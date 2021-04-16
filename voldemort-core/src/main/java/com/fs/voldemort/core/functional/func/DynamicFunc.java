package com.fs.voldemort.core.functional.func;

/**
 * @author frank
 */
@FunctionalInterface
public interface DynamicFunc<R> {
    R call(Object... params);
}
