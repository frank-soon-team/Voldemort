package com.fs.voldemort.business.paramfinder.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * The util for native method invoke
 */
public class ConstantPoolUtil {

    public static <T> Method getParameterTypeAndName(T func) throws ReflectiveOperationException {
        Object cp = invoke(func.getClass(), "getConstantPool");
        for (int i = (int) invoke(cp, "getSize") - 1; i >= 0; --i)
        {
            try {
                Member member = (Member) invoke(cp, "getMethodAt", i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class) {
                    return (Method)member;
                }
            }
            catch (Exception ignored) {
            }
        }
        throw new NoSuchMethodException();
    }

    private static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException {
        for (Method method : objClass.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    /**
     *
     * @param obj
     * @param methodName
     * @param args
     * @return
     * @throws ReflectiveOperationException
     */
    private static Object invoke(Object obj, String methodName, Object... args) throws ReflectiveOperationException {
        Field overrideField = AccessibleObject.class.getDeclaredField("override");

        overrideField.setAccessible(true);
        Method targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);

        return targetMethod.invoke(obj, args);
    }

}
