package com.fs.voldemort.core.functional.func;

import java.io.Serializable;

@FunctionalInterface
public interface Func2<T1, T2, R> {
    R call(T1 t1, T2 t2);
}
