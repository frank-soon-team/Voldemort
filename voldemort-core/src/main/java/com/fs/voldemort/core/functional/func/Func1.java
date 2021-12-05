package com.fs.voldemort.core.functional.func;

import java.io.Serializable;

@FunctionalInterface
public interface Func1<T,R> {
    
    R call(T t);
    
}
