package com.fs.voldemort.business.fit;

import com.fs.voldemort.business.BFuncParameter;
import com.fs.voldemort.business.paramfinder.ParamFinderLibrary;
import com.fs.voldemort.business.paramfinder.ParamFindResult;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.util.ConstructorHolder;
import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.support.CallerParameter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Param fit library
 * #Definition
 *  R func(
 *      Part 1-> Result arg
 *      Part 2-> Context arg
 *      Part 3-> Operate function
 *  )
 *
 * #Part1  & Part2
 * Get arg info of target function method, and adapt result that has been executed by last function:
 * ## Role 1
 *     Last function    call    Current function
 *     R func1()        --->    func2(R arg)
 * ## Role 2
 *     Last function    call    Current function
 *     R func1()        --->    func2(R arg,   C1 arg1, C2 arg2, C2 arg3)
 *  ### Parameter description
 *     R arg                    -> The result of last function
 *     C1 arg1  context param   -> The arg of context param
 *                                 Context param class is C1.class
 *                                 Context param key is     'arg1'
 *     C2 arg2                  -> Class: C1.class      key:'arg2'
 *     C2 arg3                  -> Class: C2.class      key:'arg3'
 *
 * #Part3
 * The above has been described the role which fill the result and parameters into the current function,
 * but there is still such a scene, developer need to manipulate context parameters in the function, that`s
 * why BFunc provides the entry of the operation function in the formal parameter;
 * For example base on above # role 2 current function:
 *  func2(R arg,  C1 arg1, C2 arg2, C2 arg3, {@link com.fs.voldemort.business.support.BFuncOperate} Func2<String,Object,Boolean> f_setC)
 *
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

        final List<PArg> resultArgs = ParamFinderLibrary.methodParamFinder.getParam(funcMethod).stream().map(arg -> {
            Object value = param.getParameter(arg.getParamName());
            if (value == null) {
                value = param.context().get(arg.getParamName());
            }
            return value == null ? new PArg(arg.getParamName(), arg.getParamClazz())
                    : new PArg(arg.getParamName(), arg.getParamClazz(), value);
        }).collect(Collectors.toList());
        return resultArgs.toArray();
    };

    public static final Func2<Class<?>, CallerParameter, Object[]> CUSTOM_FIT_FUNC = (clazz,param) ->{

        final Method funcMethod = OBTAIN_METHOD.call(clazz);
        if(funcMethod == null)
            return EMPTY_RESULT;

        //Get custom class
        final IFit iFit = ConstructorHolder.getNew(funcMethod.getAnnotation(BFunc.class).iFit());
        if(iFit == null)
            throw new CrucioException("Please check custom fit func!");

        return iFit.fit(funcMethod,param);
    };
}
