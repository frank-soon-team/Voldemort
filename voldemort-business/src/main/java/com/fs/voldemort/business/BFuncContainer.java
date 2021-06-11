package com.fs.voldemort.business;

import com.fs.voldemort.business.support.*;
import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Frank
 */
public class BFuncContainer implements BFuncOperational, BFuncInitializable {

    private final Map<Class<?>, BFunc> funcContainer;

    public BFuncContainer(){
        funcContainer = new ConcurrentHashMap<>();
    }

    @Override
    public BFunc getFunc(final Class<?> funcClazz) {
        return funcContainer.get(funcClazz);
    }

    @Override
    public void addFunc(Class<?> funcClazz, BFunc func) {
        funcContainer.put(funcClazz,func);
    }

    @Override
    public BFuncOperational init(Func1<Class<? extends Annotation>, Collection<Object>> getBusinessFuncHorcruxesFunc) {
        funcContainer.putAll(BFuncRegistry.scanFuncByAnnotation.call(getBusinessFuncHorcruxesFunc));
        return this;
    }

    @Override
    public BFuncOperational init(Collection<Object> businessFuncHorcruxes) {
        funcContainer.putAll(BFuncRegistry.scanFunc.call(businessFuncHorcruxes));
        return this;
    }
}