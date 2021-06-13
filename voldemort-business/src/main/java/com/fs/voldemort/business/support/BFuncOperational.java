package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BFunc;
import com.fs.voldemort.core.functional.action.Action2;
import com.fs.voldemort.core.functional.func.Func1;

public interface BFuncOperational extends BFuncAvailable, BFuncAddable {

    default Func1<Class<?>, BFunc> getFunc() {
        return this::getFunc;
    }

    default Action2<Class<?>, BFunc> addFunc() {
        return this::addFunc;
    }

}
