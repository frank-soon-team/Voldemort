package com.fs.voldemort.core.functional.action;

@FunctionalInterface
public interface Action1<T1> {

    void apply(T1 t1);
    
}
