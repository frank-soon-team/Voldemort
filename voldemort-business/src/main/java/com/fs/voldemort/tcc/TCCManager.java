package com.fs.voldemort.tcc;

import java.util.List;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.exception.TCCTimeoutException;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;
import com.fs.voldemort.tcc.state.TCCStatus;
import com.fs.voldemort.tcc.strategy.ICancelCompensateStrategy;
import com.fs.voldemort.tcc.strategy.IConfirmCompensateStrategy;

/**
 * TCC分为三个阶段：<br />
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
public abstract class TCCManager extends FuncLinkedList {

    private final static String TCC_EXECUTE_STATE = "TCC_EXECUTE_STATE";

    private IStateManager stateManager;
    private IConfirmCompensateStrategy confirmCompensateStrategy;
    private ICancelCompensateStrategy cancelCompensateStrategy;

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

        TCCExecuteState state = new TCCExecuteState(this.size());
        currentParameter.context().set(TCC_EXECUTE_STATE, state);

        // 新建一条TCC状态记录
        stateManager.begin(state);
        try {
            currentParameter = prepare(currentParameter, startNode);
            state.setStatus(TCCStatus.TrySuccess);
        } catch(ExecuteCallerNodeException e) {
            state.collectExceptional(e);
        }

        // 保存try阶段的数据
        stateManager.update(state);

        System.out.println("status: " + state.getStatus());
        
        if(state.isConfirm()) {
            commit(state);
        } else {
            rollback(state);
        }
        // 更新TCC执行结果
        stateManager.end(state);

        throwIfExceptional(state);

        return currentParameter;
    }

    //#region TCC Transactional

    protected CallerParameter prepare(CallerParameter parameter, CallerNode startNode) {
        checkOverflow(this.size());

        CallerParameter currentParameter = parameter;
        CallerNode currentNode = startNode;

        TCCExecuteState tccState = getTCCState(currentParameter);

        while(currentNode != null) {
            Object result = null;
            if(currentNode instanceof TCCNode) {
                result = executeTCCNode((TCCNode)currentNode, currentParameter);
            } else {
                result = executeNormalNode(currentNode, currentParameter);
            }

            try {
                result = tryCallSubCaller(result);
            } catch(RuntimeException e) {
                tccState.setStatus(TCCStatus.TryFaild);
                throw new ExecuteCallerNodeException(e, currentNode, currentParameter);
            }

            currentParameter = createCallParameter(currentParameter, result);
            currentNode = currentNode.getNextNode();
        }

        return currentParameter;
    }

    protected void commit(ITCCState state) {
        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            return;
        }

        TCCExecuteState tccState = (TCCExecuteState) state;
        try {
            for (TCCNode tccNode : triedNodeList) {
                tccNode.doConfirm();
            }
            tccState.setStatus(TCCStatus.ConfirmSuccess);
        } catch(RuntimeException e) {
            tccState.setStatus(e instanceof TCCTimeoutException ? TCCStatus.TryTimeout : TCCStatus.TryFaild);
            confirmCompensateStrategy.retry(state);
        }
    }

    protected void rollback(ITCCState state) {
        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            return;
        }

        TCCExecuteState tccState = (TCCExecuteState) state;
        try {
            int count = triedNodeList.size();
            for(int i = count - 1; i >= 0; i--) {
                TCCNode tccNode = triedNodeList.get(i);
                tccNode.doCancel();
            }
            tccState.setStatus(TCCStatus.CancelSuccess);
        } catch(RuntimeException e) {
            tccState.setStatus(e instanceof TCCTimeoutException ? TCCStatus.TryTimeout : TCCStatus.TryFaild);
            cancelCompensateStrategy.retry(state);
        }
    }

    //#endregion

    /**
     * 执行TCC节点
     * @param node TCC业务节点
     * @param parameter 参数
     * @return 返回执行结果
     */
    protected Object executeTCCNode(TCCNode node, CallerParameter parameter) {
        TCCExecuteState state = (TCCExecuteState) getTCCState(parameter);
        try {
            node.setNodeParameter(parameter);
            return node.doAction(parameter);
        } catch(RuntimeException e) {
            state.setStatus(e instanceof TCCTimeoutException ? TCCStatus.TryTimeout : TCCStatus.TryFaild);
            throw new ExecuteCallerNodeException(e, node, parameter);
        } finally {
            state.addTriedNode(node);
        }
    }

    /**
     * 执行普通节点
     * @param node 业务节点
     * @param parameter 参数
     * @return 返回执行结果
     */
    protected Object executeNormalNode(CallerNode node, CallerParameter parameter) {
        try {
            return node.doAction(parameter);
        } catch(RuntimeException e) {
            TCCExecuteState state = (TCCExecuteState) getTCCState(parameter);
            state.setStatus(TCCStatus.TryFaild);
            throw new ExecuteCallerNodeException(e, node, parameter);
        }
    }
    
    protected void throwIfExceptional(ITCCState state) {
        ExecuteCallerNodeException exception = state.getCallerNodeException();
        if(exception != null) {
            throw exception;
        }
    }

    private TCCExecuteState getTCCState(CallerParameter parameter) {
        return (TCCExecuteState) parameter.context().get(TCC_EXECUTE_STATE);
    }
    
}
