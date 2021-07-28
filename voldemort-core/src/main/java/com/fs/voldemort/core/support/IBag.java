package com.fs.voldemort.core.support;

import java.util.Set;

public interface IBag<K, V> {
    
    V get(K key);

    void set(K key, V value);

    boolean remove(K key);

    boolean contains(K key);

    Set<K> getKeys();

    boolean isEmpty();

}
