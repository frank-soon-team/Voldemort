package com.fs.voldemort.core.support;

import com.fs.voldemort.core.functional.func.DynamicFunc;
import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.functional.func.Func3;
import com.fs.voldemort.core.functional.func.Func4;
import com.fs.voldemort.core.functional.func.Func5;
import com.fs.voldemort.core.functional.func.Func6;
import com.fs.voldemort.core.functional.func.Func7;

public class CallerContext {
    
    private ValueBag valueBag = new ValueBag();
    private FunctionBag functionBag = null;
    private CallerContext parentContext;

    public CallerContext() {
        functionBag = new FunctionBag();
    }

    public CallerContext(CallerContext parentContext) {
        this.parentContext = parentContext;
    }

    /**
     * 获取值
     * 优先从当前上下文中获取，如果没有则尝试去parentContext中获取
     * @param key
     */
    public Object get(String key) {
        Object value = valueBag.get(key);
        if(value == null && parentContext != null) {
            value = parentContext.get(key);
        }
        return value;
    }

    /**
     * 将值放入Context中，如果key已经存在，则覆盖
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        valueBag.set(key, value);
    }

    /**
     * 更新Context，如果key不存在则不会放入value
     * @param key
     */
    public boolean update(String key, Object value) {
        if(valueBag.contains(key)) {
            valueBag.set(key, value);
            return true;
        }

        if(parentContext != null) {
            return parentContext.update(key, value);
        }

        return false;
    }

    public <R> void declareFunction(String functionName, Func0<R> func) {
        putFunction(functionName, args -> func.call());
    }

    @SuppressWarnings("unchecked")
    public <T, R> void declareFunction(String functionName, Func1<T, R> func) {
        putFunction(functionName, args -> func.call((T) args[0]));
    }

    @SuppressWarnings("unchecked")
    public <T1, T2, R> void declareFunction(String functionName, Func2<T1, T2, R> func) {
        putFunction(functionName, args -> func.call((T1) args[0], (T2) args[1]));
    }

    @SuppressWarnings("unchecked")
    public <T1, T2, T3, R> void declareFunction(String functionName, Func3<T1, T2, T3, R> func) {
        putFunction(functionName, args -> func.call((T1) args[0], (T2) args[1], (T3) args[2]));
    }

    @SuppressWarnings("unchecked")
    public <T1, T2, T3, T4, R> void declareFunction(String functionName, Func4<T1, T2, T3, T4, R> func) {
        putFunction(functionName, args -> func.call((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3]));
    }

    @SuppressWarnings("unchecked")
    public <T1, T2, T3, T4, T5, R> void declareFunction(String functionName, Func5<T1, T2, T3, T4, T5, R> func) {
        putFunction(functionName, args -> func.call((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4]));
    }

    @SuppressWarnings("unchecked")
    public <T1, T2, T3, T4, T5, T6, R> void declareFunction(String functionName, Func6<T1, T2, T3, T4, T5, T6, R> func) {
        putFunction(functionName, args -> func.call((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5]));
    }

    @SuppressWarnings("unchecked")
    public <T1, T2, T3, T4, T5, T6, T7, R> void declareFunction(String functionName, Func7<T1, T2, T3, T4, T5, T6, T7, R> func) {
        putFunction(functionName, args -> func.call((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5], (T7) args[6]));
    }

    public void putFunction(String functionName, DynamicFunc<Object> func) {
        if(parentContext != null) {
            parentContext.putFunction(functionName, func);
        } else {
            functionBag.set(functionName, func);
        }
    }
    
    public <R> R callFunction(String functionName, Object... args) {
        return functionBag.call(functionName, args);
    }

    public <R> R callFunctionWithDefault(String functionName, Object... args) {
        return functionBag.tryCall(functionName, args, null);
    }

    public <R> R callFunctionWithDefault(String functionName, R defaultResult, Object... args) {
        return functionBag.tryCall(functionName, args, defaultResult);
    }

}
