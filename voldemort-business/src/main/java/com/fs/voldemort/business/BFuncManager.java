package com.fs.voldemort.business;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.fs.voldemort.business.exception.BusinessFuncInitializationException;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

public class BFuncManager {

    protected static Func1<Class<?>, BFunc> getFunc;

    public static void initByAnnotation(Func1<Class<? extends Annotation>, Collection<Object>> getBusinessFuncHorcruxesFunc) {
        getFunc = new BFuncContainer().init(getBusinessFuncHorcruxesFunc).getFunc();
    }

    public static void init(Collection<Object> businessFuncHorcruxesFuncs) {
        getFunc = new BFuncContainer().init(businessFuncHorcruxesFuncs).getFunc();
    }

    public static Object invokeFunc(CallerParameter callerParameter, Class<?> funcClazz) {
        if(getFunc == null) {
            throw new BusinessFuncInitializationException("The businessFuncAvailableCaller initialize error, please check component config.");
        }

        final BFunc bFunc = getFunc.call(funcClazz);
        if(bFunc == null) {
            throw new BusinessFuncInitializationException("Can not find func, please ensure funcClazz: " + funcClazz.getName() + " has been config correctly.");
        }

        Object[] result = bFunc.paramFitFunc.call(funcClazz, callerParameter);
        return bFunc.func.call(result);
    }
    
}
