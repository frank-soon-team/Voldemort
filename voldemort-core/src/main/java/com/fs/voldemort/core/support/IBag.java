package com.fs.voldemort.core.support;

public interface IBag<K, V> {
    
    V get(K key);

    void set(K key, V value);

}
