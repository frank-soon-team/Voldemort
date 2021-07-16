package com.fs.voldemort.core.support;

import java.math.BigDecimal;
import java.util.Date;

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
    private FunctionBag functionBag;
    private CallerContext parentContext;

    public CallerContext() {
        functionBag = new FunctionBag();
    }

    public CallerContext(CallerContext parentContext) {
        this.parentContext = parentContext;
    }

    //#region Value

    public Object get(String key) {
        return get(
            key, valueBag::get, parentContext != null ? parentContext::get : null);
    }

    public Byte getByte(String key) {
        return get(
            key, valueBag::getByteValue, parentContext != null ? parentContext::getByte : null);
    }

    public Character getChar(String key) {
        return get(
            key, valueBag::getCharValue, parentContext != null ? parentContext::getChar : null);
    }

    public String getString(String key) {
        return get(
            key, valueBag::getStringValue, parentContext != null ? parentContext::getString : null);
    }

    public Boolean getBoolean(String key) {
        return get(
            key, valueBag::getBooleanValue, parentContext != null ? parentContext::getBoolean : null);
    }

    public Short getShort(String key) {
        return get(
            key, valueBag::getShortValue, parentContext != null ? parentContext::getShort : null);
    }

    public Integer getInteger(String key) {
        return get(
            key, valueBag::getIntegerValue, parentContext != null ? parentContext::getInteger : null);
    }

    public Long getLong(String key) {
        return get(
            key, valueBag::getLongValue, parentContext != null ? parentContext::getLong : null);
    }

    public Float getFloat(String key) {
        return get(
            key, valueBag::getFloatValue, parentContext != null ? parentContext::getFloat : null);
    }

    public Double getDouble(String key) {
        return get(
            key, valueBag::getDoubleValue, parentContext != null ? parentContext::getDouble : null);
    }

    public BigDecimal getBigDecimal(String key) {
        return get(
            key, valueBag::getBigDecimalValue, parentContext != null ? parentContext::getBigDecimal : null);
    }

    public Date getDate(String key) {
        return get(
            key, valueBag::getDateValue, parentContext != null ? parentContext::getDate : null);
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

    /**
     * 获取值
     * 优先从当前上下文中获取，如果没有则尝试去parentContext中获取
     * @param key
     */
    protected <T1, R> R get(T1 key, Func1<T1, R> selfFunc, Func1<T1, R> parentFunc) {
        R value = selfFunc.call(key);
        if(value == null && parentFunc != null) {
            value = parentFunc.call(key);
        }
        return value;
    }

    //#endregion

    //#region Function

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
        if(parentContext != null) {
            return parentContext.callFunction(functionName, args);
        }
        return functionBag.call(functionName, args);
    }

    public <R> R callFunctionWithDefault(String functionName, Object... args) {
        if(parentContext != null) {
            return parentContext.callFunctionWithDefault(functionName, args);
        }
        return functionBag.tryCall(functionName, args, null);
    }

    public <R> R callFunctionWithDefault(String functionName, R defaultResult, Object... args) {
        if(parentContext != null) {
            return parentContext.callFunctionWithDefault(functionName, defaultResult, args);
        }
        return functionBag.tryCall(functionName, args, defaultResult);
    }

    //#endregion

}
