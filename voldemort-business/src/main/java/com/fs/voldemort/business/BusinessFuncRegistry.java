package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFuncMark;
import com.fs.voldemort.core.exception.CallerException;
import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public class BusinessFuncRegistry {

    private BusinessFuncRegistry(){}

    public static final Func1<Func1<Class<? extends Annotation>, Map<String, Object>>,Map<Class<?>, BusinessFunc>> scanFuncByAnnotation =
        getBusinessFuncHorcruxesFunc -> BusinessFuncRegistry.scanFunc.call(getBusinessFuncHorcruxesFunc.call(BusinessFuncHorcruxes.class));

    public static final Func1<Map<String, Object>, Map<Class<?>, BusinessFunc>> scanFunc =
        funcHorcruxesBeanMap -> {
            if (funcHorcruxesBeanMap.isEmpty()) {
                return null;
            }

            final Integer funcHorcruxesInstanceSize = funcHorcruxesBeanMap.size();

            final Map<Method, BusinessFuncCallable> assistFuncHorcruxesInstanceMap = new HashMap<>(funcHorcruxesInstanceSize);
            return
                funcHorcruxesBeanMap.values().stream()
                    .filter(BusinessFuncCallable.class::isInstance)
                    .map(BusinessFuncCallable.class::cast)
                    .map(
                        funcHorcruxes -> Arrays.stream(funcHorcruxes.getClass().getDeclaredMethods())
                            .filter(method -> {
                                if(method.isAnnotationPresent(BusinessFuncMark.class)){
                                    assistFuncHorcruxesInstanceMap.put(method, funcHorcruxes);
                                        return true;
                                }
                                return false;
                            })
                            .collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(
                        Collectors.toMap(Method::getClass,
                            method -> new BusinessFunc(
                                assistFuncHorcruxesInstanceMap.get(method).getClass(),
                                args -> {
                                    try {
                                        return method.invoke(assistFuncHorcruxesInstanceMap.get(method),args);
                                    } catch (Exception e) {
                                        throw new CallerException(e.getMessage(),e);
                                    }
                                },
                                p -> assistFuncHorcruxesInstanceMap.get(method).paramFit(p)
                            )
                        )
                    );
        };
}
