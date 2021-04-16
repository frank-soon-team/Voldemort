package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.core.functional.func.Func1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Frank
 */
public class BusinessFuncContainer{

    private final Map<Class<?>, BusinessFunc> funcContainer;

    public BusinessFuncContainer(){
        funcContainer = new ConcurrentHashMap<>();
    }

    public void fill(Func1<Class<?>, Map<String, BusinessFuncCallable>> getBusinessFuncHorcruxesFunc) {
        funcContainer.putAll(BusinessFuncRegistry.scanFunc.call(getBusinessFuncHorcruxesFunc));
    }

    public void fillHook(){

    }

    public BusinessFunc getFunc(final Class<?> funcClazz) {
        return funcContainer.get(funcClazz);
    }

}