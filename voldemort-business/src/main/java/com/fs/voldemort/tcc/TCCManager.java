package com.fs.voldemort.tcc;

import java.util.ArrayList;
import java.util.List;

import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.exception.TCCStateException;
import com.fs.voldemort.tcc.exception.TCCTimeoutException;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;
import com.fs.voldemort.tcc.state.TCCStatus;
import com.fs.voldemort.tcc.state.TCCTaskStatus;
import com.fs.voldemort.tcc.strategy.ICompensateStrategy;

/**
 * TCC分为三个阶段：<br>
 * <ol>
 * <li>
 * Try阶段：资源预留，Try阶段成功则事务一定会成功
 * </li>
 * <li>
 * Confirm阶段：Try阶段全部成功后，更新状态
 * </li>
 * <li>
 * Cancel阶段：Try阶段失败后，对预留的资源释放
 * </li>
 * </ol>
 * 
 * <p>
 * TCCCaller中只能包含TCC节点和普通节点，TCC节点为ITCCHandler，普通节点会在Try阶段执行。
 * </p>
 * <p>
 * 注意：普通节点抛出异常也会导致TCC节点回滚。
 * </p>
 * 
 */
public class TCCManager extends FuncLinkedList {

    private Func0<ITCCState> tccStateGetter;
    private IStateManager stateManager;
    private ICompensateStrategy compensateStrategy;

    public TCCManager(ITCCManagerAdapter adapter) {
        if(adapter == null) {
            throw new IllegalArgumentException("the parameter [adapter] of constructor is required.");
        }

        this.stateManager = adapter.getStateManager();
        this.compensateStrategy = adapter.getCompensateStrategy();
        this.tccStateGetter = adapter.getTCCStateGetter();
        if(this.tccStateGetter == null) {
            this.tccStateGetter = () -> createTCCState();
        }
    }

    //#region getter

    public IStateManager getStateManager() {
        return stateManager;
    }

    public ICompensateStrategy getConfirmCompensateStrategy() {
        return compensateStrategy;
    }

    //#endregion

    public void add(ITCCHandler tccHandler) {
        if(tccHandler == null) {
            throw new IllegalArgumentException("the parameter tccHandler is required.");
        }

        TCCNode node = new TCCNode(tccHandler);
        add(node);
    }

    @Override
    public CallerParameter execute(CallerParameter parameter) {
        CallerParameter currentParameter = ensureCallerParameter(parameter);
        if(this.isEmpty()) {
            return createCallParameter(currentParameter, null);
        }

        ITCCState state = this.tccStateGetter.call();
        setTCCState(currentParameter, state);

        if(state.getTaskStatus() == TCCTaskStatus.Start && state.getStatus() == TCCStatus.TryPending) {
            // 新建的TCC任务记录，状态 > Start，TCC状态 > TryPending
            stateManager.begin(state);
            
            // * try 阶段 （一阶段）
            currentParameter = prepare(currentParameter, getFirstNode());

            // 更新try阶段TCC任务记录，状态 > Start, TCC状态 > TrySuccess | TryFaild
            // 中间状态，没必要落库 stateManager.update(state);
        } else {
            // 准备进行二阶段补偿，更新任务记录
            getTCCExecuteState(state).setTaskStatus(TCCTaskStatus.Start);
            stateManager.update(state);
        }
        
        // * 确认阶段 （二阶段）或 补偿
        if(state.isConfirm()) {
            commit(state);
        } else {
            rollback(state);
        }

        // 更新确认阶段TCC执行结果，状态 > Done | Imcomplete，TCC状态 > ConfirmSuccess | ConfirmFailed | CancelSuccess | CancelFailed
        stateManager.end(state);

        // 二阶段失败，补偿
        if(!state.isSuccess()) {
            compensate(state);
        }

        throwIfExceptional(state);

        return currentParameter;
    }

    //#region TCC Transactional

    protected CallerParameter prepare(CallerParameter parameter, CallerNode startNode) {
        checkOverflow(this.size());

        CallerParameter currentParameter = parameter;
        CallerNode currentNode = startNode;

        TCCExecuteState tccState = getTCCExecuteState(currentParameter);

        try {
            while(currentNode != null) {
                Object result = null;
                if(currentNode instanceof TCCNode) {
                    TCCNode tccNode = (TCCNode) currentNode;
                    tccNode.setNodeParameter((TCCNodeParameter) currentParameter);
                    result = tccNode.doAction(currentParameter);
                    tccNode.setStatus(TCCStatus.TrySuccess);
                } else {
                    result = currentNode.doAction(currentParameter);
                }
                // 执行子Caller
                result = tryCallSubCaller(result);
    
                currentParameter = createCallParameter(currentParameter, result);
                currentNode = currentNode.getNextNode();
            }
            tccState.setStatus(TCCStatus.TrySuccess);
        } catch(RuntimeException e) {
            if(currentNode instanceof TCCNode) {
                TCCNode tccNode = (TCCNode) currentNode;
                tccNode.setStatus(e instanceof TCCTimeoutException ? TCCStatus.TryTimeout : TCCStatus.TryFaild);
            }
            tccState.setStatus(TCCStatus.TryFaild);
            tccState.collectExceptional(new ExecuteCallerNodeException(e, currentNode, currentParameter));
        }

        return currentParameter;
    }

    protected void commit(ITCCState state) {
        TCCExecuteState tccState = (TCCExecuteState) state;

        List<TCCNode> triedNodeList = tccState.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            throw new IllegalStateException("something wrong, the tried NodeList is empty, can not commit.");
        }

        TCCStatus status = tccState.getStatus();
        if(status == TCCStatus.TrySuccess || status == TCCStatus.ConfirmFailed || status == TCCStatus.ConfirmTimeout) {
            List<TCCNode> commitFailedList = new ArrayList<>(triedNodeList.size());

            for (TCCNode tccNode : triedNodeList) {
                if(tccNode.getStatus() == TCCStatus.ConfirmSuccess) {
                    continue;
                }
                try {
                    tccNode.doConfirm();
                    tccNode.setStatus(TCCStatus.ConfirmSuccess);
                } catch(RuntimeException e) {
                    tccNode.setStatus(e instanceof TCCTimeoutException ? TCCStatus.ConfirmTimeout : TCCStatus.ConfirmFailed);
                    tccNode.setError(e instanceof TCCTimeoutException ? (TCCTimeoutException) e : new ExecuteCallerNodeException(e, tccNode));
                    commitFailedList.add(tccNode);
                }
            }

            if(commitFailedList.isEmpty()) {
                tccState.setStatus(TCCStatus.ConfirmSuccess);
                tccState.setTaskStatus(TCCTaskStatus.Done);
            } else {
                tccState.setStatus(TCCStatus.ConfirmFailed);
                tccState.setTaskStatus(TCCTaskStatus.Incomplete);
            }
        } else {
            tccState.setStatus(TCCStatus.ConfirmFailed);
            tccState.setTaskStatus(TCCTaskStatus.Incomplete);
            tccState.setStateException(
                new TCCStateException("TCC confirm stage error", TCCStatus.TrySuccess.getValue(), tccState.getStatus().getValue()));
        }
    }

    protected void rollback(ITCCState state) {
        TCCExecuteState tccState = (TCCExecuteState) state;
        
        List<TCCNode> triedNodeList = tccState.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            throw new IllegalStateException("something wrong, the tried NodeList is empty, can not rollback.");
        }

        TCCStatus status = tccState.getStatus();
        if((status == TCCStatus.TryFaild || status == TCCStatus.TryTimeout) 
            || (status == TCCStatus.CancelFailed || status == TCCStatus.CancelTimeout)) {
            int count = triedNodeList.size();
            List<TCCNode> rollbackFailedList = new ArrayList<>(count);

            for(int i = count - 1; i >= 0; i--) {
                TCCNode tccNode = triedNodeList.get(i);
                if(tccNode.getStatus() == TCCStatus.CancelSuccess) {
                    continue;
                }
                try {
                    tccNode.doCancel();
                    tccNode.setStatus(TCCStatus.CancelSuccess);
                } catch(RuntimeException e) {
                    tccNode.setStatus(e instanceof TCCTimeoutException ? TCCStatus.CancelTimeout : TCCStatus.CancelFailed);
                    tccNode.setError(e instanceof TCCTimeoutException ? (TCCTimeoutException) e : new ExecuteCallerNodeException(e, tccNode));
                    rollbackFailedList.add(tccNode);
                }
            }

            if(rollbackFailedList.isEmpty()) {
                tccState.setStatus(TCCStatus.CancelSuccess);
                tccState.setTaskStatus(TCCTaskStatus.Done);
            } else {
                tccState.setStatus(TCCStatus.CancelFailed);
                tccState.setTaskStatus(TCCTaskStatus.Incomplete);
            }
        } else {
            tccState.setStatus(TCCStatus.CancelFailed);
            tccState.setTaskStatus(TCCTaskStatus.Incomplete);
            tccState.setStateException(
                new TCCStateException("TCC cancel stage error", TCCStatus.TrySuccess.getValue(), tccState.getStatus().getValue()));
        }
    }

    protected void compensate(ITCCState tccState) {
        if(compensateStrategy != null) {
            compensateStrategy.retry(tccState);
        }
    }

    //#endregion
    
    @Override
    protected CallerParameter ensureCallerParameter(CallerParameter parameter) {
        if(parameter == null) {
            return new TCCNodeParameter(null, new CallerContext());
        }
        return new TCCNodeParameter(parameter.result, parameter.context());
    }

    @Override
    protected CallerParameter createCallParameter(CallerParameter oldParameter, Object result) {
        return new TCCNodeParameter(result, oldParameter.context());
    }

    protected ITCCState createTCCState() {
        TCCExecuteState state = new TCCExecuteState(this.size());
        // 提取TCCNode节点
        CallerNode currentNode = this.getFirstNode();
        while(currentNode != null) {
            if(currentNode instanceof TCCNode) {
                state.addTCCNode((TCCNode)currentNode);
            }
            currentNode = currentNode.getNextNode();
        }

        return state;
    }

    protected void throwIfExceptional(ITCCState state) {
        ExecuteCallerNodeException exception = state.getCallerNodeException();
        if(exception != null) {
            throw exception;
        }
    }

    private void setTCCState(CallerParameter parameter, ITCCState tccState) {
        ((TCCNodeParameter) parameter).setTCCState(tccState);
    }

    private TCCExecuteState getTCCExecuteState(CallerParameter parameter) {
        return (TCCExecuteState) ((TCCNodeParameter) parameter).getTCCState();
    }

    private TCCExecuteState getTCCExecuteState(ITCCState state) {
        if(state instanceof TCCExecuteState) {
            return (TCCExecuteState) state;
        }
        throw new ClassCastException("the parameter state is not TCCExecuteState Type.");
    }
    
}
