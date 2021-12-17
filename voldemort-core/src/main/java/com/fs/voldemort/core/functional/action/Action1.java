package com.fs.voldemort.core.functional.action;

import java.io.Serializable;

@FunctionalInterface
public interface Action1<T1> extends Serializable {

    void apply(T1 t1);
    
}
