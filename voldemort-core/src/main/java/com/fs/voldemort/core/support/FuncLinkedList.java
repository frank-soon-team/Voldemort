package com.fs.voldemort.core.support;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.func.Func;

public class FuncLinkedList {

    protected final static int OVERFLOW_COUNT = 1024;

    private CallerNode firstNode;
    private CallerNode lastNode;
    private int _size = 0;
    
    public FuncLinkedList() {
    }

    public void add(Func<CallerParameter, Object> func) {
        if(func == null) {
            throw new IllegalArgumentException("the parameter func is required.");
        }

        CallerNode node = new CallerNode(func);
        add(node);
    }

    protected void add(CallerNode node) {
        _size++;

        if(firstNode == null) {
            firstNode = node;
            return;
        }

        if(lastNode == null) {
            lastNode = node;
            firstNode.setNextNode(node);
            return;
        }

        lastNode.setNextNode(node);
        lastNode = node;
    }

    public CallerParameter execute(CallerParameter parameter) {
        checkOverflow(this.size());

        CallerParameter currentParameter = ensureCallerParameter(parameter);
        CallerNode currentNode = firstNode;
        
        while(currentNode != null) {
            try {
                Object result = currentNode.doAction(currentParameter);
                int index = 1;
                while(result instanceof Caller) {
                    checkOverflow(index);
                    result = ((Caller) result).exec(currentParameter);
                    index++;
                }
                currentParameter = createCallParameter(currentParameter, result);
                currentNode = currentNode.getNextNode();
            } catch(Throwable e) {
                throw new ImperioException("caller excute error.", e);
            }
        }

        return currentParameter;
    }

    public CallerNode getFirstNode() {
        return this.firstNode;
    }

    public CallerNode getLastNode() {
        return this.lastNode;
    }

    public int size() {
        return _size;
    }
    
    protected CallerParameter ensureCallerParameter(CallerParameter parameter) {
        CallerParameter result = null;
        if(parameter == null) {
            result = new CallerParameter(null, new CallerContext());
        } else {
            result = new CallerParameter(parameter);
        }

        return result;
    }

    protected CallerParameter createCallParameter(CallerParameter oldParameter, Object result) {
        CallerParameter newParameter = new CallerParameter(result, oldParameter.context());
        return newParameter;
    }

    protected void checkOverflow(int index) {
        if(index > OVERFLOW_COUNT) {
            throw new CrucioException("overflow");
        }
    }
    
}
