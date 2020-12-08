package com.fs.voldemort.core.support;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValueBag implements IBag<String, Object> {

    private Map<String, Object> contextStore = new LinkedHashMap<>(16);

    @Override
    public Object get(String key) {
        return contextStore.get(key);
    }

    @Override
    public void set(String key, Object value) {
        contextStore.put(key, value);
    }
    
}
