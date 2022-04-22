package com.fs.voldemort.core.support;

import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.functional.func.Func2;

public class CallerNode {

    private Func1<CallerParameter, CallerParameter> beforeFunc;
    
    private Func1<CallerParameter, Object> actionFunc;

    private Func2<CallerParameter, Object, Object> afterFunc;

    private CallerNode previousNode;

    private CallerNode nextNode;

    public CallerNode(Func1<CallerParameter, Object> actionFunc) {
        setActionFunc(actionFunc);
    }

    //#region getter and setter

    public CallerNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(CallerNode previousNode) {
        this.previousNode = previousNode;
    }

    public CallerNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(CallerNode nextNode) {
        this.nextNode = nextNode;
    }

    public void setActionFunc(Func1<CallerParameter, Object> actionFunc) {
        if(actionFunc == null) {
            throw new IllegalArgumentException("the parameter [actionFunc] is required.");
        }
        this.actionFunc = actionFunc;
    }

    public Func1<CallerParameter, Object> getActionFunc() {
        return this.actionFunc;
    }

    public Func1<CallerParameter, CallerParameter> getBeforeFunc() {
        return beforeFunc;
    }

    public void setBeforeFunc(Func1<CallerParameter, CallerParameter> beforeFunc) {
        this.beforeFunc = beforeFunc;
    }

    public Func2<CallerParameter, Object, Object> getAfterFunc() {
        return afterFunc;
    }

    public void setAfterFunc(Func2<CallerParameter, Object, Object> afterFunc) {
        this.afterFunc = afterFunc;
    }

    //#endregion

    public boolean isFirst() {
        return previousNode == null && nextNode != null;
    }

    public boolean isLast() {
        return previousNode != null && nextNode == null;
    }

    public Object doAction(CallerParameter callerParameter) {
        callerParameter = onBefore(callerParameter);

        Object result = null;
        if(actionFunc != null) {
            result = actionFunc.call(callerParameter);
        }
        
        result = onAfter(callerParameter, result);

        return result;
    }

    protected CallerParameter onBefore(CallerParameter callerParameter) {
        if(beforeFunc != null) {
            return beforeFunc.call(callerParameter);
        }
        return callerParameter;
    }

    protected Object onAfter(CallerParameter callerParameter, Object result) {
        if(afterFunc != null) {
            return afterFunc.call(callerParameter, result);
        }
        return result;
    }

    
}
