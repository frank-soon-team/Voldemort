package com.fs.voldemort.action;

@FunctionalInterface
public interface Action1<T1> {

    void apply(T1 param);
    
}
