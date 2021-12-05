package com.fs.voldemort.business;

import com.fs.voldemort.core.functional.action.Action0;
import com.fs.voldemort.core.functional.action.Action1;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.function.Consumer;

public class LambdaFunnyTest {

    @Test
    public void test_Lambda() throws ReflectiveOperationException {

//        Consumer<String> a = new Consumer<String>()
//        {
//            @Override
//            public void accept(String s)
//            {
//                System.out.println(s);
//            }
//        };
//
//        Class lambdaClazz = a.getClass();

        Action1<?> b = (String s) -> System.out.println(s);

        Class lambdaClazz = b.getClass();


        System.out.println(getConsumerParameterType(b));

    }

    public Class<?> getErased(Type type)
    {
        if (type instanceof ParameterizedType)
        {
            return getErased(((ParameterizedType) type).getRawType());
        }
        if (type instanceof GenericArrayType)
        {
            return Array.newInstance(getErased(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        }
        if (type instanceof TypeVariable<?>)
        {
            var bounds = ((TypeVariable<?>) type).getBounds();
            return bounds.length > 0 ? getErased(bounds[0]) : Object.class;
        }
        if (type instanceof WildcardType)
        {
            var bounds = ((WildcardType) type).getUpperBounds();
            return bounds.length > 0 ? getErased(bounds[0]) : Object.class;
        }
        if (type instanceof Class<?>)
        {
            return (Class<?>) type;
        }
        return Object.class;
    }

    public Class<?> getConsumerParameterType(Action1<?> consumer) throws ReflectiveOperationException
    {
        for (var type : consumer.getClass().getGenericInterfaces())
        {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Consumer.class)
            {
                return getErased(((ParameterizedType) type).getActualTypeArguments()[0]);
            }
        }
        if (consumer.getClass().isSynthetic())
        {
            System.out.println(consumer.getClass().getName() + "is synthetic!");
            return getConsumerLambdaParameterType(consumer);
        }
        throw new NoSuchMethodException();
    }

    public static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException
    {
        for (var method : objClass.getDeclaredMethods())
        {
            if (methodName.equals(method.getName()))
            {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public static Object invoke(Object obj, String methodName, Object... args) throws ReflectiveOperationException
    {
        var overrideField = AccessibleObject.class.getDeclaredField("override");
        overrideField.setAccessible(true);
        var targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);
        return targetMethod.invoke(obj, args);
    }

    public static Class<?> getConsumerLambdaParameterType(Action1<?> consumer) throws ReflectiveOperationException
    {
        var consumerClass = consumer.getClass();
        var constantPool = invoke(consumerClass, "getConstantPool");
        for (var i = (int) invoke(constantPool, "getSize") - 1; i >= 0; --i)
        {
            try
            {
                var member = (Member) invoke(constantPool, "getMethodAt", i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class)
                {
                    return ((Method) member).getParameterTypes()[0];
                }
            }
            catch (Exception ignored)
            {
                // ignored
            }
        }
        throw new NoSuchMethodException();
    }
}
