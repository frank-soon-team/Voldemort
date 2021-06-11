package com.fs.voldemort.business;

import com.fs.voldemort.core.functional.func.DynamicFunc;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

/**
 * The structure that holding business function info
 */
public class BFunc {

    public final Class<?> funcClazz;

    public final DynamicFunc<?> func;

    public final Func1<CallerParameter,Object[]> paramFitFunc;

    public BFunc(Class<?> funcClass, DynamicFunc<?> func,
                 Func1<CallerParameter,Object[]> paramFitFunc) {
        this.funcClazz = funcClass;
        this.func = func;
        this.paramFitFunc = paramFitFunc;
    }
}
