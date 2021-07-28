package com.fs.voldemort.tcc;

import java.util.ArrayList;
import java.util.List;

import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.exception.TCCTimeoutException;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;
import com.fs.voldemort.tcc.state.TCCStatus;
import com.fs.voldemort.tcc.strategy.ICancelCompensateStrategy;
import com.fs.voldemort.tcc.strategy.IConfirmCompensateStrategy;

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

    private IStateManager stateManager;
    private IConfirmCompensateStrategy confirmCompensateStrategy;
    private ICancelCompensateStrategy cancelCompensateStrategy;

    protected TCCManager() {

    }

    public TCCManager(ITCCManagerAdapter adapter) {
        if(adapter == null) {
            throw new IllegalArgumentException("the parameter [adapter] of constructor is required.");
        }

        setStateManager(adapter.getStateManager());
        setConfirmCompensateStrategy(adapter.getConfirmCompensateStrategy());
        setCancelCompensateStrategy(adapter.getCancelCompensateStrategy());
    }

    public IStateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(IStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public IConfirmCompensateStrategy getConfirmCompensateStrategy() {
        return confirmCompensateStrategy;
    }

    public void setConfirmCompensateStrategy(IConfirmCompensateStrategy confirmCompensateStrategy) {
        this.confirmCompensateStrategy = confirmCompensateStrategy;
    }

    public ICancelCompensateStrategy getCancelCompensateStrategy() {
        return cancelCompensateStrategy;
    }

    public void setCancelCompensateStrategy(ICancelCompensateStrategy cancelCompensateStrategy) {
        this.cancelCompensateStrategy = cancelCompensateStrategy;
    }

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
        CallerNode startNode = getFirstNode();
        if(startNode == null) {
            return createCallParameter(currentParameter, null);
        }

        ITCCState state = createTCCState(startNode);
        setTCCState(currentParameter, state);

        // 新建一条TCC状态记录
        stateManager.begin(state);
        
        // * try 阶段 （一阶段）
        currentParameter = prepare(currentParameter, startNode);

        // 更新try阶段TCC状态记录
        stateManager.update(state);
        
        // * 确认阶段 （二阶段）
        if(state.isConfirm()) {
            commit(state);
        } else {
            rollback(state);
        }

        // 更新确认阶段TCC执行结果
        stateManager.end(state);

        // 二阶段失败，补偿
        if(!state.isSuccess()) {
            compensate(state);
        }

        throwIfExceptional(state);

        return currentParameter;
    }

    public void confirm(ITCCState tccState) {
        if(tccState == null) {
            throw new IllegalArgumentException("the parameter tccState is required.");
        }

        commit(tccState);
        stateManager.end(tccState);
    }

    public void cancel(ITCCState tccState) {
        if(tccState == null) {
            throw new IllegalArgumentException("the parameter tccState is required.");
        }

        rollback(tccState);
        stateManager.end(tccState);
    }

    //#region TCC Transactional

    protected CallerParameter prepare(CallerParameter parameter, CallerNode startNode) {
        checkOverflow(this.size());

        CallerParameter currentParameter = parameter;
        CallerNode currentNode = startNode;

        TCCExecuteState tccState = getTCCState(currentParameter);

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
        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            return;
        }

        TCCExecuteState tccState = (TCCExecuteState) state;
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
                commitFailedList.add(tccNode);
            }
        }

        if(commitFailedList.isEmpty()) {
            tccState.setStatus(TCCStatus.ConfirmSuccess);
        } else {
            tccState.setStatus(TCCStatus.ConfirmFailed);
        }
    }

    protected void rollback(ITCCState state) {
        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            return;
        }

        TCCExecuteState tccState = (TCCExecuteState) state;
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
                rollbackFailedList.add(tccNode);
            }
        }

        if(rollbackFailedList.isEmpty()) {
            tccState.setStatus(TCCStatus.CancelSuccess);
        } else {
            tccState.setStatus(TCCStatus.CancelFailed);
        }
    }

    protected void compensate(ITCCState tccState) {
        TCCStatus status = tccState.getStatus();
        
        if(status == TCCStatus.ConfirmFailed || status == TCCStatus.ConfirmTimeout) {
            if(confirmCompensateStrategy != null) {
                confirmCompensateStrategy.retry(tccState);
            }
            return;
        }

        if(status == TCCStatus.CancelFailed || status == TCCStatus.CancelTimeout) {
            if(cancelCompensateStrategy != null) {
                cancelCompensateStrategy.retry(tccState);
            }
            return;
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

    protected ITCCState createTCCState(CallerNode startNode) {
        TCCExecuteState state = new TCCExecuteState(this.size());
        // 提取TCCNode节点
        CallerNode currentNode = startNode;
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

    private TCCExecuteState getTCCState(CallerParameter parameter) {
        return (TCCExecuteState) ((TCCNodeParameter) parameter).getTCCState();
    }
    
}
