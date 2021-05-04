package com.fs.voldemort.business.support;

import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface BusinessFuncInitializable {

    BusinessFuncOperational init(Func1<Class<? extends Annotation>, Map<String, Object>> getBusinessFuncHorcruxesFunc);

}
