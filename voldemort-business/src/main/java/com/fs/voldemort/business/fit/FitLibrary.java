package com.fs.voldemort.business.fit;

import com.fs.voldemort.business.BFuncParameter;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.business.util.ConstructorHolder;
import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.support.CallerParameter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Param fit library
 */
public class FitLibrary {

    public static final Object[] EMPTY_RESULT = new Object[0];

    public static final Func1<Class<?>,Method> OBTAIN_METHOD = clazz -> {
        final List<Method> funcMethodList = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(BFunc.class)))
                .collect(Collectors.toList());

        if(funcMethodList.size() > 1) {
            throw new CrucioException("Settle function can only have one func method!");
        }else if(funcMethodList.isEmpty()) {
            return null;
        }

        return funcMethodList.get(0);
    };

    public static final Func2<Class<?>, CallerParameter, Object[]> AUTO_FIT_FUNC = (clazz,callerParameter) ->{

        final BFuncParameter param = (BFuncParameter)callerParameter;

        final Method funcMethod = OBTAIN_METHOD.call(clazz);
        if(funcMethod == null)
            return EMPTY_RESULT;

        //arg result set
        final List<Object> arg = new LinkedList<>();
        //Context arg temporary set
        final List<CArg> cArgSet = new LinkedList<>();
        //Param arg temporary set
        final List<PArg> pArgSet = Arrays.stream(funcMethod.getParameters()).filter(p->{
            if(p.isAnnotationPresent(BFuncOperate.class)){
                cArgSet.add(new CArg(p.getAnnotation(BFuncOperate.class).value(), param.context(), param.result));
                return false;
            }
            return true;
        }).map(p-> new PArg(p.getName())).collect(Collectors.toList());

        if(!pArgSet.isEmpty()) {
            //Deal context/nodeparam arg
            pArgSet.stream().forEach(pArg -> {
                pArg.value = param.getParameter(pArg.name);
                if (pArg.value == null) {
                    pArg.value = param.context().get(pArg.name);
                }
            });

            arg.addAll(pArgSet.stream().map(pArg -> pArg.value).collect(Collectors.toList()));
        }

        //Deal context operator func arg
        if(!cArgSet.isEmpty()) {
            arg.addAll(cArgSet.stream()
                    .map(cArg->cArg.oper == BFuncOperate.Oper.RESULT ? cArg.result : cArg.getOperFunc())
                    .collect(Collectors.toList()));
        }

        return arg.toArray();
    };

    public static final Func2<Class<?>, CallerParameter, Object[]> CUSTOM_FIT_FUNC = (clazz,param) ->{

        final Method funcMethod = OBTAIN_METHOD.call(clazz);
        if(funcMethod == null)
            return EMPTY_RESULT;

        //Get custom class
        final IFit iFit = ConstructorHolder.getNew(funcMethod.getAnnotation(BFunc.class).iFit());
        if(iFit == null)
            throw new CrucioException("Please check ");

        return iFit.fit(funcMethod,param);
    };
}
