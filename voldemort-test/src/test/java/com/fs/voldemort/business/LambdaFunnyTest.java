package com.fs.voldemort.business;

import com.fs.voldemort.core.functional.action.Action1;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LambdaFunnyTest {

    @Test
    public void test_Lambda() throws ReflectiveOperationException {
        Action1<?> b = (String s) -> {
            System.out.println(s);
        };

        Collection<ParamInfo> paramInfos = getParam(b);
        for (ParamInfo paramInfo : paramInfos) {
            System.out.println(paramInfo.getParamClass().getName());
            System.out.println(paramInfo.getParamName());
        }
    }

    public <T> Collection<ParamInfo> getParam(T func) throws ReflectiveOperationException {
        if (func.getClass().isSynthetic()) {
            return getParameterTypeAndName(func);
        }
        throw new NoSuchMethodException();
    }

    public static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException {
        for (Method method : objClass.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public static Object invoke(Object obj, String methodName, Object... args) throws ReflectiveOperationException {
        Field overrideField = AccessibleObject.class.getDeclaredField("override");
        overrideField.setAccessible(true);
        Method targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);
        return targetMethod.invoke(obj, args);
    }

    public static <T> Collection<ParamInfo> getParameterTypeAndName(T func) throws ReflectiveOperationException {
        Object cp = invoke(func.getClass(), "getConstantPool");
        for (int i = (int) invoke(cp, "getSize") - 1; i >= 0; --i)
        {
            try {
                Member member = (Member) invoke(cp, "getMethodAt", i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class) {
                    return getParam((Method)member);
                }
            }
            catch (Exception ignored) {
            }
        }
        throw new NoSuchMethodException();
    }


    public static Collection<ParamInfo> getParam(final Method method) throws NotFoundException, ClassNotFoundException {

        if(method == null) {
            throw new RuntimeException("Method Cannot be null!");
        }

        final String methodName = method.getName();
        final Class clazz = method.getDeclaringClass();

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(clazz.getName());
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new NotFoundException("cannot get LocalVariableAttribute");
        }

        Collection<ParamInfo> params = new LinkedList<>();

        int staticPos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        CtClass[] ctClazzes = cm.getParameterTypes();
        int indexRelatively = 0;
        for (CtClass paramClazz : ctClazzes) {
            params.add(new LambdaFuncParam(Class.forName(paramClazz.getName()) , attr.variableName(indexRelatively++ + staticPos)));
        }
        return params;
    }

    public static interface ParamInfo{
        String getParamName();
        Class<?> getParamClass();
    }

    public static class LambdaFuncParam implements ParamInfo{

        /**
         * The class of param
         */
        private Class<?> clazz;

        /**
         * The name of param
         */
        private String name;

        public LambdaFuncParam(){
        }

        public LambdaFuncParam(Class<?> clazz, String name){
            this.clazz = clazz;
            this.name = name;
        }

        @Override
        public String getParamName() {
            return this.name;
        }

        @Override
        public Class<?> getParamClass() {
            return this.clazz;
        }
    }
}
