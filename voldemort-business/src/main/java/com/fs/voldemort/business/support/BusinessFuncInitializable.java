package com.fs.voldemort.business.support;

import com.fs.voldemort.core.functional.func.Func1;

import java.util.Map;

public interface BusinessFuncInitializable {

    void init(Func1<Class<?>, Map<String, BusinessFuncCallable>> getBusinessFuncHorcruxesFunc);

}
