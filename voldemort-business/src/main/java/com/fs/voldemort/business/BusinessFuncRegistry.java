package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFuncMark;
import com.fs.voldemort.core.exception.CallerException;
import com.fs.voldemort.core.functional.func.Func1;

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

    public static Func1<Func1<Class<?>, Map<String, BusinessFuncCallable>>,Map<Class<?>, BusinessFunc>> scanFunc = (getBusinessFuncHorcruxesFunc) -> {
        final Map<String, BusinessFuncCallable> funcHorcruxesBeanMap = getBusinessFuncHorcruxesFunc.call(BusinessFuncHorcruxes.class);
        if (funcHorcruxesBeanMap.isEmpty()) {
            return null;
        }

        final Integer funcHorcruxesInstanceSize = funcHorcruxesBeanMap.size();

        final Map<Method, BusinessFuncCallable> assistFuncHorcruxesInstanceMap = new HashMap<>(funcHorcruxesInstanceSize);
        return
            funcHorcruxesBeanMap.values().stream()
                .map(
                    funcHorcruxes -> Arrays.stream(funcHorcruxes.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(BusinessFuncMark.class))
                        .peek(method -> assistFuncHorcruxesInstanceMap.put(method, funcHorcruxes))
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
                                    throw new CallerException(e.getMessage());
                                }
                            },
                            p -> assistFuncHorcruxesInstanceMap.get(method).paramFit(p)
                        )
                    )
                );
    };
}
