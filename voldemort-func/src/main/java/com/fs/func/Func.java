package com.fs.func;

import java.util.Objects;

@FunctionalInterface
public interface Func<T,R> {
    R call(T t);

    default <V> Func<T, V> then(Func<R,V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.call(call(t));
    }
}
