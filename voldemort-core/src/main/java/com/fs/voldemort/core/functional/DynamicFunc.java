package com.fs.voldemort.core.functional;

import java.io.Serializable;

/**
 * @author frank
 */
@FunctionalInterface
public interface DynamicFunc<R> extends Serializable {
    R call(Object... params);
}
