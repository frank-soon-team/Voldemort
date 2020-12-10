package com.fs.voldemort.tcc;

import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.func.Func;

public class TCCManager extends FuncLinkedList {

    public void add(ITCCHandler tccHandler) {
        if(tccHandler == null) {
            throw new IllegalArgumentException("the parameter tccHandler is required.");
        }

        TCCNode node = new TCCNodeWrapper(
            null, //actionFunc, 
            null, //cancelFunc,
            tccHandler.getClass().getSimpleName() + ":Node:" + size()
        );

        add(node);
    }

    @Override
    public CallerParameter execute(CallerParameter parameter) {
        CallerParameter currentParameter = ensureCallerParameter(parameter);
        CallerNode currentNode = getFirstNode();
        CallerNode errorNode = null;

        if(currentNode == null) {
            return createCallParameter(currentParameter, null);
        }

        beginTransaction();

        int count = 0;
        while(currentNode != null) {
            if(count > 1024) {
                throw new CrucioException("overflow");
            }
            try {
                Object result = currentNode.doAction(currentParameter);
                currentParameter = createCallParameter(currentParameter, result);
                currentNode = currentNode.getNextNode();
                count++;
            } catch(Throwable e) {
                errorNode = currentNode;
            }
        }

        if(errorNode != null) {
            doCancel(currentParameter, errorNode);
        }

        endTransaction();
        throwIfHasErrors();

        return currentParameter;
    }

    // protected Object executeTCCNode(TCCNode node) {

    // }

    // protected Object executeNormalNode(CallerNode node) {

    // }

    protected String getTCCNodeKey(TCCNode node) {
        return ((TCCNodeWrapper)node).key();
    }

    protected void beginTransaction() {
        // Insert initail status data
    }

    protected void endTransaction() {
        // Update completed status data
    }

    protected void collectError() {

    }

    protected void throwIfHasErrors() {

    }

    private void doCancel(CallerParameter parameter, CallerNode errorNode) {
        CallerNode cancelNode = errorNode;
        CallerParameter currentParameter = parameter;
        while(true) {
            if(cancelNode instanceof TCCNode) {
                TCCNode node = (TCCNode)cancelNode;
                String tccNodeKey = getTCCNodeKey(node);
                currentParameter = createCallParameter(currentParameter, currentParameter.context().get(tccNodeKey));
                try {
                    node.doCancel(currentParameter);
                } catch (Exception e) {

                }
            }

            if(cancelNode.isFirst()) {
                break;
            }
        }
    }

    private static class TCCNodeWrapper extends TCCNode {

        private String key;

        public TCCNodeWrapper(Func<CallerParameter, Object> actionFunc, Func<CallerParameter, Object> cancelFunc, String key) {
            super(actionFunc, cancelFunc);
            this.key = key;
        }

        public String key() {
            return this.key;
        }
        
    }
    
}
