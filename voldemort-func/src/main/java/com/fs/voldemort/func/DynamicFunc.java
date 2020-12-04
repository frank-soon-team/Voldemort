package com.fs.voldemort.func;

/**
 * @author frank
 */
@FunctionalInterface
public interface DynamicFunc<R> {
    R call(Object... params);
}
