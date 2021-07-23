package com.fs.voldemort.core.support;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fs.voldemort.core.functional.func.DynamicFunc;

public class FunctionBag implements IBag<String, DynamicFunc<Object>> {

    private Map<String, DynamicFunc<Object>> contextStore = new LinkedHashMap<>(16);

    @Override
    public DynamicFunc<Object> get(String key) {
        if(key == null || key.length() == 0) {
            return null;
        }
        return contextStore.get(key);
    }

    @Override
    public void set(String key, DynamicFunc<Object> value) {
        if(key == null || key.length() == 0) {
            throw new IllegalArgumentException("the parameter key is required.");
        }
        contextStore.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return contextStore.containsKey(key);
    }

    @Override
    public Set<String> getKeys() {
        return new HashSet<>(contextStore.keySet());
    }

    @Override
    public boolean isEmpty() {
        return contextStore.isEmpty();
    }

    public <R> R call(String key, Object[] args) {
        if(key == null || key.length() == 0) {
            throw new IllegalArgumentException("the parameter key is required.");
        }
        if(!contains(key)) {
            throw new IllegalArgumentException("the parameter key [" + key + "] is not exists.");
        }

        return doCall(key, args);
    }

    public <R> R tryCall(String key, Object[] args, R defaultResult) {
        if(key == null || key.length() == 0 || !contains(key)) {
            return defaultResult;
        }

        R result = doCall(key, args);
        if(result == null) {
            return defaultResult;
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    protected <R> R doCall(String key, Object[] args) {
        DynamicFunc<Object> dynamicFunc = get(key);
        if(dynamicFunc == null) {
            return null;
        }

        return (R) dynamicFunc.call(args);
    }
    
}
