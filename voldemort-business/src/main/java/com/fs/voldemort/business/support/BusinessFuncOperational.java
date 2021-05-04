package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BusinessFunc;
import com.fs.voldemort.core.functional.action.Action2;
import com.fs.voldemort.core.functional.func.Func1;

public interface BusinessFuncOperational extends BusinessFuncAvailable,BusinessFuncAddable{

    default Func1<Class<?>, BusinessFunc> getFunc() {
        return this::getFunc;
    }

    default Action2<Class<?>, BusinessFunc> addFunc() {
        return this::addFunc;
    }

}
