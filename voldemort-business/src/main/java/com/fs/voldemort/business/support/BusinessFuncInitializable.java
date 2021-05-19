package com.fs.voldemort.business.support;

import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface BusinessFuncInitializable {

    BusinessFuncOperational init(Func1<Class<? extends Annotation>, Collection<Object>> getBusinessFuncHorcruxesFunc);

    BusinessFuncOperational init(Collection<Object> businessFuncHorcruxesFunc);

}
