package com.fs.voldemort.core;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.core.support.ShareContextCallerParameter;

public class Caller {

    protected final FuncLinkedList funcList;
    private final CallerParameter initailizationParameter;

    public Caller() {
        this(new FuncLinkedList(), null);
    }

    public Caller(CallerParameter initParameter) {
        this(new FuncLinkedList(), initParameter);
    }

    public Caller(CallerParameter initParameter, boolean shareContext) {
        this(shareContext 
            ? new ShareContextCallerParameter(initParameter.result, initParameter.context()) 
            : initParameter);
    }

    protected Caller(FuncLinkedList funcLinkedList) {
        this(funcLinkedList, null);
    }

    protected Caller(FuncLinkedList funcLinkedList, CallerParameter initCallerParameter) {
        if(funcLinkedList == null) {
            throw new IllegalArgumentException("the parameter funcLinkedList of constructor is required.");
        }
        funcList = funcLinkedList;
        initailizationParameter = initCallerParameter;
    }

    public Caller call(Func1<CallerParameter, Object> func) {
        funcList.add(func);
        return this;
    }

    public Caller call(Caller caller) {
        if(caller == null) {
            throw new IllegalArgumentException("the parameter caller is required.");
        }

        funcList.add(p -> caller.exec(p));
        return this;
    }

    public void exec(Action1<Object> action) {
        if(action == null) {
            throw new IllegalArgumentException("the parameter action is required.");
        }
        Object result = exec();
        action.apply(result);
    }

    @SuppressWarnings("unchecked")
    public <R> R exec() {
        return (R) exec(initailizationParameter);
    }
    
    protected Object exec(CallerParameter parameter) {
        CallerParameter resultParam = funcList.execute(parameter);
        return resultParam.result;
    }
    
    public static Caller create(Func0<Object> rootAct) {
        Caller caller = create();
        if(rootAct != null) {
            caller.funcList.add(p -> rootAct.call());
        }
        return caller;
    }

    public static Caller create() {
        return new Caller();
    }

}
