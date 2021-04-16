package com.fs.voldemort.core.support;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.action.Action2;
import com.fs.voldemort.core.functional.func.Func1;

public class CallerNode {

    private Action1<CallerParameter> beforeAction;
    
    private Func1<CallerParameter, Object> actionFunc;

    private Action2<CallerParameter, Object> afterAction;

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
            throw new IllegalArgumentException("the parameter actionFunc is required.");
        }
        this.actionFunc = actionFunc;
    }

    public Func1<CallerParameter, Object> getActionFunc() {
        return this.actionFunc;
    }

    public Action1<CallerParameter> getBeforeAction() {
        return beforeAction;
    }

    public void setBeforeAction(Action1<CallerParameter> beforeAction) {
        this.beforeAction = beforeAction;
    }

    public Action2<CallerParameter, Object> getAfterAction() {
        return afterAction;
    }

    public void setAfterAction(Action2<CallerParameter, Object> afterAction) {
        this.afterAction = afterAction;
    }

    //#endregion

    public boolean isFirst() {
        return previousNode == null && nextNode != null;
    }

    public boolean isLast() {
        return previousNode != null && nextNode == null;
    }

    public Object doAction(CallerParameter callerParameter) {
        onBefore(callerParameter);

        Object result = null;
        if(actionFunc != null) {
            result = actionFunc.call(callerParameter);
        }
        
        onAfter(callerParameter, result);

        return result;
    }

    protected void onBefore(CallerParameter callerParameter) {
        if(beforeAction != null) {
            beforeAction.apply(callerParameter);
        }
    }

    protected void onAfter(CallerParameter callerParameter, Object result) {
        if(afterAction != null) {
            afterAction.apply(callerParameter, result);
        }
    }

    
}
