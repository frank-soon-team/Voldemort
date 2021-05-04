package com.fs.voldemort.business;

import com.fs.voldemort.business.support.*;
import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Frank
 */
public class BusinessFuncContainer implements BusinessFuncOperational, BusinessFuncInitializable {

    private final Map<Class<?>, BusinessFunc> funcContainer;

    public BusinessFuncContainer(){
        funcContainer = new ConcurrentHashMap<>();
    }

    public void fill(Func1<Class<? extends Annotation>, Map<String, Object>> getBusinessFuncHorcruxesFunc) {
        funcContainer.putAll(BusinessFuncRegistry.scanFunc.call(getBusinessFuncHorcruxesFunc));
    }

    @Override
    public BusinessFunc getFunc(final Class<?> funcClazz) {
        return funcContainer.get(funcClazz);
    }

    @Override
    public void addFunc(Class<?> funcClazz, BusinessFunc func) {
        funcContainer.put(funcClazz,func);
    }

    @Override
    public BusinessFuncOperational init(Func1<Class<? extends Annotation>, Map<String, Object>> getBusinessFuncHorcruxesFunc) {
        fill(getBusinessFuncHorcruxesFunc);
        return this;
    }
}