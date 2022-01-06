package com.fs.voldemort.core.functional.action;

import java.io.Serializable;

@FunctionalInterface
public interface Action3<T1, T2, T3> extends Serializable {

    void apply(T1 t1, T2 t2, T3 t3);
    
}
