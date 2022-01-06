package com.fs.voldemort.business;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.fs.voldemort.business.exception.BusinessFuncInitializationException;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

public class BFuncManager {

    protected static Func1<Class<?>, BFunc> f_getBFunc;

    protected static Func1<String,?> f_getIocInstanceByName;

    protected static Func1<Class,?> f_getIocInstanceByClass;

    public static void initByAnnotation(Func1<Class<? extends Annotation>, Collection<Object>> getBusinessFuncHorcruxesFunc) {
        f_getBFunc = new BFuncContainer().init(getBusinessFuncHorcruxesFunc).getFunc();
    }

    public static void init(Collection<Object> businessFuncHorcruxesFuncCollection) {
        f_getBFunc = new BFuncContainer().init(businessFuncHorcruxesFuncCollection).getFunc();
    }

    public static void initIocInstanceGetFunc(Func1<Class,?> getIocInstanceByClazzFunc, Func1<String,?> getIocInstanceByNameFunc) {
        BFuncManager.f_getIocInstanceByClass = getIocInstanceByClazzFunc;
        BFuncManager.f_getIocInstanceByName = getIocInstanceByNameFunc;
    }

    public static Object getIocInstanceByClazz(Class clazz) {
        if(f_getIocInstanceByClass == null) {
            throw new BusinessFuncInitializationException("Can not get ioc instance, please ensure BFuncManager#initGetBeanFunc has been invoked!");
        }
        return f_getIocInstanceByClass.call(clazz);
    }

    public static Object getIocInstanceByName(String name) {
        if(f_getIocInstanceByClass == null) {
            throw new BusinessFuncInitializationException("Can not get ioc instance, please ensure BFuncManager#initGetBeanFunc has been invoked!");
        }
        return f_getIocInstanceByName.call(name);
    }

    public static Object invokeFunc(CallerParameter callerParameter, Class<?> funcClazz) {
        if(f_getBFunc == null) {
            throw new BusinessFuncInitializationException("The businessFuncAvailableCaller initialize error, please check component config.");
        }
        final BFunc bFunc = f_getBFunc.call(funcClazz);
        if(bFunc == null) {
            throw new BusinessFuncInitializationException("Can not find func, please ensure funcClazz: " + funcClazz.getName() + " has been config correctly.");
        }
        Object[] result = bFunc.paramFitFunc.call(funcClazz, callerParameter);
        return bFunc.func.call(result);
    }
}
