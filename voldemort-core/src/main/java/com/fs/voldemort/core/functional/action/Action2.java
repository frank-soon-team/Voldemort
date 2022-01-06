package com.fs.voldemort.core.functional.action;

import java.io.Serializable;

@FunctionalInterface
public interface Action2<T1, T2> extends Serializable {

    void apply(T1 t1, T2 t2);
    
}
