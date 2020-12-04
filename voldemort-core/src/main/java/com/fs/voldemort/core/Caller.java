package com.fs.voldemort.core;

import com.fs.voldemort.core.support.Param;
import com.fs.voldemort.func.Act;
import com.fs.voldemort.func.Func;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Caller {

    private Func callFunc;

    private final Map<String,Object> context = new HashMap<>();

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

//    public static void main(String[] args) {
//        Caller
//            .create()
//            .call(p -> {
//                System.out.println("call1:"+p.result);
//                p.context.set("c_key","c_value1");
//                return new BigDecimal("1");
//            })
//            .call(p -> {
//                System.out.println("call2:"+p.result);
//                p.context.set("c_num_key","c_num_value");
//                return ((BigDecimal)p.result).add(new BigDecimal("1"));
//            })
//            .call(p->{
//                System.out.println("call3:"+p.result);
//                System.out.println("p.context.c_key:"+p.context.get("c_key"));
//                System.out.println("p.context.c_num_key"+p.context.getBigDecimal("c_num_key"));
//                return "success";
//            })
//            .exec(System.out::println);
//    }

}
