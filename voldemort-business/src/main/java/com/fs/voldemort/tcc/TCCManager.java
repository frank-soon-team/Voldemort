package com.fs.voldemort.tcc;

import java.util.List;

import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;

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
public class TCCManager extends FuncLinkedList {

    private final static String TCC_EXECUTE_STATE = "TCC_EXECUTE_STATE";

    private IStateManager stateManager;

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

        stateManager.begin(state);
        try {
            currentParameter = prepare(currentParameter, startNode);
        } catch(ExecuteCallerNodeException e) {
            state.collectExceptional(e);
        }

        // isRollback和isEnd互斥，只能满足其中一个
        if(state.isRollback()) {
            rollback(state);
        }
        if(state.isEnd()) {
            commit(state);
        }
        stateManager.end(state);

        throwIfExceptional(state);

        return currentParameter;
    }

    //#region TCC Transactional

    protected CallerParameter prepare(CallerParameter parameter, CallerNode startNode) {
        CallerParameter currentParameter = parameter;
        CallerNode currentNode = startNode;

        int count = 0;
        while(currentNode != null) {
            if(count > OVERFLOW_COUNT) {
                throw new CrucioException("overflow");
            }

            Object result = null;
            if(currentNode instanceof TCCNode) {
                result = executeTCCNode((TCCNode)currentNode, currentParameter);
            } else {
                result = executeNormalNode(currentNode, currentParameter);
            }

            count++;
            currentParameter = createCallParameter(currentParameter, result);
            currentNode = currentNode.getNextNode();
        }

        TCCExecuteState state = getTCCState(currentParameter);
        state.setRollback(false);
        state.setEnd(true);

        return currentParameter;
    }

    protected void commit(ITCCState state) {
        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            return;
        }

        for (TCCNode tccNode : triedNodeList) {
            tccNode.doConfirm();
        }
    }

    protected void rollback(ITCCState state) {
        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            return;
        }

        int count = triedNodeList.size();
        for(int i = count - 1; i >= 0; i++) {
            TCCNode tccNode = triedNodeList.get(i);
            tccNode.doCancel();
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
            state.setRollback(true);
            throw e;
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
            state.setRollback(true);
            throw e;
        }
    }
    
    protected void throwIfExceptional(ITCCState state) {
        
    }

    private TCCExecuteState getTCCState(CallerParameter parameter) {
        return (TCCExecuteState) parameter.context().get(TCC_EXECUTE_STATE);
    }
    
}
