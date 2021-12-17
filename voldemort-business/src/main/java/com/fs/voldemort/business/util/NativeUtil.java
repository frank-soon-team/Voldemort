package com.fs.voldemort.business.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The util for native method invoke
 */
public class NativeUtil {

    private static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException {
        for (Method method : objClass.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    private static Object invoke(Object obj, String methodName, Object... args) throws ReflectiveOperationException {
        Field overrideField = AccessibleObject.class.getDeclaredField("override");

        overrideField.setAccessible(true);
        Method targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);
        return targetMethod.invoke(obj, args);
    }



}
