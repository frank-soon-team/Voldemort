package com.fs.voldemort.business.paramfinder.util;

import com.fs.voldemort.business.paramfinder.ParamFindResult;
import com.fs.voldemort.business.paramfinder.SimpleFindResult;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class JavassistUtil {

    public static Collection<ParamFindResult> getParam(final Method method) throws NotFoundException, ClassNotFoundException{
        if(method == null) {
            throw new IllegalArgumentException("Method Cannot be null!");
        }
        final String methodName = method.getName();
        final Class<?> clazz = method.getDeclaringClass();
        final AnnotatedType[] at = method.getAnnotatedParameterTypes();

        CtMethod cm = ClassPool.getDefault().get(clazz.getName()).getDeclaredMethod(methodName);
        LocalVariableAttribute attr = (LocalVariableAttribute) cm.getMethodInfo().getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new NotFoundException("Cannot get LocalVariableAttribute!");
        }

        Collection<ParamFindResult> params = new LinkedList<>();
        int staticPos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        CtClass[] ctClazzes = cm.getParameterTypes();

        for(int i=0; i<ctClazzes.length; i++) {
            Collection<Annotation> annotations = at[i].getAnnotations().length == 0 ? new ArrayList<>()
                    : Arrays.asList(at[i].getAnnotations());
            params.add(new SimpleFindResult(attr.variableName(i + staticPos), Class.forName(ctClazzes[i].getName()), annotations));
        }

        return params;
    }
}
