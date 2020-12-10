package com.fs.voldemort.core;

import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.func.Act;
import com.fs.voldemort.func.Func;

import java.util.function.Consumer;

public class Caller {

    protected final FuncLinkedList funcList;

    public Caller() {
        funcList  = new FuncLinkedList();
    }

    protected Caller(FuncLinkedList funcLinkedList) {
        if(funcLinkedList == null) {
            throw new IllegalArgumentException("the parameter funcLinkedList is required.");
        }
        funcList = funcLinkedList;
    }

    public static Caller create(Act<Object> rootAct) {
        Caller caller = create();
        if(rootAct != null) {
            caller.funcList.add(p -> rootAct.act());
        }
        return caller;
    }

    public static Caller create() {
        return new Caller();
    }

    public Caller call(Func<CallerParameter, Object> func) {
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

    public void exec(Consumer<Object> consumer) {
        if(consumer == null) {
            throw new IllegalArgumentException("the parameter consumer is required.");
        }
        Object result = exec();
        consumer.accept(result);
    }

    @SuppressWarnings("unchecked")
    public <R> R exec() {
        return (R) exec((CallerParameter) null);
    }
    
    protected Object exec(CallerParameter parameter) {
        CallerParameter resultParam = funcList.execute(parameter);
        return resultParam.result;
    }

}
