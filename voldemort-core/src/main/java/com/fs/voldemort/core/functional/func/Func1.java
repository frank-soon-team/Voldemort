package com.fs.voldemort.core.functional.func;

@FunctionalInterface
public interface Func1<T,R> {
    
    R call(T t);
    
}
