package com.fs.voldemort.business.paramfinder;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParamFindResultStore {

    private static final Map<Class<?>, Collection<ParamFindResult>> lambdaStore = new ConcurrentHashMap<>(100);

    private static final Map<Method, Collection<ParamFindResult>> methodStore = new ConcurrentHashMap<>(100);

    public static Collection<ParamFindResult> put(Class<?> clazz, Collection<ParamFindResult> results){
        lambdaStore.put(clazz,results);
        return results;
    }

    public static Collection<ParamFindResult> get(Class<?> clazz){
        return lambdaStore.get(clazz);
    }

    public static Collection<ParamFindResult> put(Method method, Collection<ParamFindResult> results){
        methodStore.put(method,results);
        return results;
    }

    public static Collection<ParamFindResult> get(Method method){
        return methodStore.get(method);
    }
}
