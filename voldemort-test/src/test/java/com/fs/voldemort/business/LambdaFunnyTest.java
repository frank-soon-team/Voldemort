package com.fs.voldemort.business;

import com.fs.voldemort.core.functional.action.Action1;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.Arrays;

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

        Action1<?> b = (String s) -> {
            System.out.println(s);
        };
        System.out.println(getConsumerParameterType(b));
    }

    public Class<?> getConsumerParameterType(Action1<?> consumer) throws ReflectiveOperationException
    {
        if (consumer.getClass().isSynthetic())
        {
            return getConsumerLambdaParameterType(consumer);
        }
        throw new NoSuchMethodException();
    }

    public static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException
    {
        for (Method method : objClass.getDeclaredMethods())
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
        Field overrideField = AccessibleObject.class.getDeclaredField("override");
        overrideField.setAccessible(true);
        Method targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);
        return targetMethod.invoke(obj, args);
    }

    public static Class<?> getConsumerLambdaParameterType(Action1<?> consumer) throws ReflectiveOperationException
    {
        Class consumerClass = consumer.getClass();
        Object constantPool = invoke(consumerClass, "getConstantPool");
        for (int i = (int) invoke(constantPool, "getSize") - 1; i >= 0; --i)
        {
            try
            {
                Member member = (Member) invoke(constantPool, "getMethodAt", i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class)
                {
                    Method method = (Method) member;
                    System.out.println(method.getName());
                    Parameter[] params = method.getParameters();
                    for(Parameter param : params) {
                        System.out.println(param.getName());
                        System.out.println(param.getType());
                    }

                    Class statementClazz = member.getDeclaringClass();
                    String[] paramsName = getParamNames(statementClazz,method.getName());
                    System.out.println(paramsName);

                    return method.getParameterTypes()[0];
                }
            }
            catch (Exception ignored)
            {
                // ignored
            }
        }
        throw new NoSuchMethodException();
    }


    public static String[] getParamNames(Class clazz, String method) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(clazz.getName());
        CtMethod cm = cc.getDeclaredMethod(method);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new NotFoundException("cannot get LocalVariableAttribute");
        }
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }


}
