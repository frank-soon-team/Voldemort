package com.fs.voldemort.business.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a class Constructor holder, which is used to speed up instantiation.
 * Support lazy or active loading
 */
@Slf4j
public class ConstructorHolder {

    private static final Map<Class<?>, Constructor<?>> constructorContainer = new ConcurrentHashMap<>();

    public static <T> Constructor<T> fill(@NonNull Class<T> clazz) throws NoSuchMethodException {
        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructorContainer.put(clazz,constructor);
        return constructor;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getNew(@NonNull Class<T> clazz){
        Constructor<T> constructor = (Constructor<T>) constructorContainer.get(clazz);
        if(constructor == null){
            try {
                constructor = fill(clazz);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Can not get new instance, error:",e);
            return null;
        }
    }
}
