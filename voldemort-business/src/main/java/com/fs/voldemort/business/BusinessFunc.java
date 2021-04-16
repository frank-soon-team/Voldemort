package com.fs.voldemort.business;

import java.util.Set;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.core.functional.func.DynamicFunc;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

public class BusinessFunc {

    public final Class<?> funcClazz;

    public final DynamicFunc<?> func;

    public final Func1<CallerParameter,Set<BusinessFuncCallable.Args>> paramFitFunc;

    public BusinessFunc(Class<?> funClass, DynamicFunc<?> func,
        Func1<CallerParameter,Set<BusinessFuncCallable.Args>> paramFitFunc) {
        this.funcClazz = funClass;
        this.func = func;
        this.paramFitFunc = paramFitFunc;
    }
}
