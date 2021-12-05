package com.fs.voldemort.core.functional;

/**
 * @author frank
 */
@FunctionalInterface
public interface DynamicFunc<R> {
    R call(Object... params);
}
