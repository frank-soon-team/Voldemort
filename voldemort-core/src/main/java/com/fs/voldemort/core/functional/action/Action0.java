package com.fs.voldemort.core.functional.action;

import java.io.Serializable;

@FunctionalInterface
public interface Action0 extends Serializable {

    void apply();
    
}
