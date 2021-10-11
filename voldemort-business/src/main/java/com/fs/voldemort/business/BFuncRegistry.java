package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public class BFuncRegistry {

    private BFuncRegistry(){}

    public static final Func1<Func1<Class<? extends Annotation>, Collection<Object>>,Map<Class<?>, com.fs.voldemort.business.BFunc>> scanFuncByAnnotation =
        getBusinessFuncHorcruxesFunc -> BFuncRegistry.scanFunc.call(getBusinessFuncHorcruxesFunc.call(BFuncHorcruxes.class));

    public static final Func1<Collection<Object>, Map<Class<?>, com.fs.voldemort.business.BFunc>> scanFunc =
        funcHorcruxesList -> {
            if (funcHorcruxesList.isEmpty()) {
                return null;
            }

            final Integer funcHorcruxesInstanceSize = funcHorcruxesList.size();

            final Map<Method, Object> assistFuncHorcruxesInstanceMap = new HashMap<>(funcHorcruxesInstanceSize);
            return
                funcHorcruxesList.stream()
                    .map(
                        funcHorcruxes -> Arrays.stream(funcHorcruxes.getClass().getDeclaredMethods())
                            .filter(method -> {
                                if(method.isAnnotationPresent(BFunc.class)){
                                    assistFuncHorcruxesInstanceMap.put(method, funcHorcruxes);
                                        return true;
                                }
                                return false;
                            })
                            .collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(
                        Collectors.toMap(Method::getDeclaringClass,
                            method -> new com.fs.voldemort.business.BFunc(
                                assistFuncHorcruxesInstanceMap.get(method).getClass(),
                                args -> {
                                    try {
                                        return method.invoke(assistFuncHorcruxesInstanceMap.get(method),args);
                                    } catch (Exception e) {
                                        throw new ImperioException(e.getMessage(),e);
                                    }
                                },
                                method.getAnnotation(BFunc.class).fit().getFitFunc()
                            )
                        )
                    );
        };
}
