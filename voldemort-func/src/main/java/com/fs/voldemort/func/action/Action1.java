package com.fs.voldemort.func.action;

@FunctionalInterface
public interface Action1<T1> {

    void apply(T1 param);
    
}
