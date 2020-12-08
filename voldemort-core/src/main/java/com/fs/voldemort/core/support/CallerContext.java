package com.fs.voldemort.core.support;

public class CallerContext {
    
    private ValueBag valueBag = new ValueBag();

    public Object get(String key) {
        return valueBag.get(key);
    }

    public void set(String key, Object value) {
        valueBag.set(key, value);
    }
    
}
