package com.fs.voldemort.core;

import com.fs.voldemort.core.support.Param;
import com.fs.voldemort.func.Act;
import com.fs.voldemort.func.Func;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Caller {

    protected Func callFunc;

    protected final Map<String,Object> context = new HashMap<>();

    protected Caller() {
    }

    public static <R> Caller create(@NonNull Act<R> rootAct) {
        Caller caller = new Caller();
        caller.callFunc = r -> new Param<>(rootAct.act(), caller.context);
        return caller;
    }

    public static Caller create(){
        Caller caller = new Caller();
        caller.callFunc = r -> new Param<>(null, caller.context);
        return caller;
    }

    @SuppressWarnings("unchecked")
    public <T,R> Caller call(@NonNull Func<Param<T>,R> callFunc) {
        Func<Param<T>,Param<R>> func = p -> new Param<>(callFunc.call(p), context);
        this.callFunc = this.callFunc.then(func);
        return this;
    }

    public void exec(@NonNull Consumer<Object> consumer) {
        Object result = exec();
        consumer.accept(result);
    }

    @SuppressWarnings("unchecked")
    public <R> R exec() {
        Param<R> param = (Param<R>)callFunc.call(null);
        return param.result;
    }

}
