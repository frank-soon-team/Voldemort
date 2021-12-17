package com.fs.voldemort.core.functional.func;

import java.io.Serializable;

@FunctionalInterface
public interface Func1<T,R> extends Serializable {
    
    R call(T t);
    
}
