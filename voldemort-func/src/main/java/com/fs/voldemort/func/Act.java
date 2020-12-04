package com.fs.voldemort.func;

@FunctionalInterface
public interface Act<R> {
    R act();
}
