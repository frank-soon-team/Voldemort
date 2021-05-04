package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncAddable;
import com.fs.voldemort.business.support.BusinessFuncAvailable;
import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncInitializable;
import com.fs.voldemort.core.functional.func.Func1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Frank
 */
public class BusinessFuncContainer implements BusinessFuncAddable, BusinessFuncInitializable, BusinessFuncAvailable {

    private final Map<Class<?>, BusinessFunc> funcContainer;

    public BusinessFuncContainer(){
        funcContainer = new ConcurrentHashMap<>();
    }

    public void fill(Func1<Class<?>, Map<String, BusinessFuncCallable>> getBusinessFuncHorcruxesFunc) {
        funcContainer.putAll(BusinessFuncRegistry.scanFunc.call(getBusinessFuncHorcruxesFunc));
    }

    @Override
    public BusinessFunc getFunc(final Class<?> funcClazz) {
        return funcContainer.get(funcClazz);
    }

    @Override
    public void add(Class<?> funcClazz, BusinessFunc func) {
        funcContainer.put(funcClazz,func);
    }

    @Override
    public void init(Func1<Class<?>, Map<String, BusinessFuncCallable>> getBusinessFuncHorcruxesFunc) {
        fill(getBusinessFuncHorcruxesFunc);
    }
}