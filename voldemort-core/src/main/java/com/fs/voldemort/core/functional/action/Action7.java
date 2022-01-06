package com.fs.voldemort.core.functional.action;

import java.io.Serializable;

@FunctionalInterface
public interface Action7<T1, T2, T3, T4, T5, T6, T7> extends Serializable {

    void apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
    
}
