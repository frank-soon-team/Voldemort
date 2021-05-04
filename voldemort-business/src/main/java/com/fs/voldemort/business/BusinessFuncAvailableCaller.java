package com.fs.voldemort.business;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.Func1;

/**
 * @author frank
 */
public abstract class BusinessFuncAvailableCaller extends Caller{

    public static Func1<Class<?>,BusinessFunc> getFunc;

}
