package com.fs.voldemort.business.fit;

import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerParameter;

public class FitContext {

    private final CallerParameter callerParameter;

    private final Func2<Class<?>, String, ?> getIocInstance;

    public FitContext(CallerParameter callerParameter, Func2<Class<?>, String, ?> getIocInstance) {
        this.callerParameter = callerParameter;
        this.getIocInstance = getIocInstance;
    }

    public CallerParameter callerParameter() {
        return callerParameter;
    }

    public CallerContext callerContext() {
        return callerParameter.context();
    }

    public Object getContext(String key) {
        return callerContext().get(key);
    }

    public Object getIocInstance(Class<?> iocInstanceClazz, String iocInstanceName) {
        return getIocInstance.call(iocInstanceClazz, iocInstanceName);
    }
}