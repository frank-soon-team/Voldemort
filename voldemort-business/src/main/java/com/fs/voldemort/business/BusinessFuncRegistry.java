package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFuncMark;
import com.fs.voldemort.core.exception.CallerException;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.func.DynamicFunc;
import com.fs.voldemort.func.Func;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public class BusinessFuncRegistry {

    private Map<Class<?>, BusinessFunc> funcContainer;

    private final Func<Class<?>, Map<String, BusinessFuncCallable>> getBusinessFuncHorcruxesFunc;

    public BusinessFuncRegistry(Func<Class<?>, Map<String, BusinessFuncCallable>> getBusinessFuncHorcruxesFunc){
        this.getBusinessFuncHorcruxesFunc = getBusinessFuncHorcruxesFunc;
        init();
        scanAndFill();
    }

    private void init(){
        funcContainer = new ConcurrentHashMap<>();
        BusinessCaller.businessFuncRegistry = this;
    }

    private void scanAndFill() {
        final Map<String, BusinessFuncCallable> funcHorcruxesBeanMap = getBusinessFuncHorcruxesFunc.call(BusinessFuncHorcruxes.class);
        if (funcHorcruxesBeanMap.isEmpty()) {
            return;
        }
        final Map<Method, BusinessFuncCallable> assistFuncHorcruxesInstanceMap = new HashMap<>(funcContainer.size());
        funcContainer.putAll(
            funcHorcruxesBeanMap.values().stream()
                .map(
                    funcHorcruxes -> {
                        return Arrays.stream(funcHorcruxes.getClass().getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(BusinessFuncMark.class))
                            .peek(method -> assistFuncHorcruxesInstanceMap.put(method, funcHorcruxes))
                            .collect(Collectors.toList());
                    }
                )
                .flatMap(Collection::stream)
                .collect(
                    Collectors.toMap(
                        method -> method.getClass(),
                        method -> {
                            return new BusinessFunc(
                                assistFuncHorcruxesInstanceMap.get(method).getClass(), 
                                args -> {
                                    try {
                                        return method.invoke(assistFuncHorcruxesInstanceMap.get(method),args);
                                    } catch (Exception e) {
                                        throw new CallerException(e.getMessage());
                                    }
                                },
                                p -> assistFuncHorcruxesInstanceMap.get(method).paramFit(p)
                            );
                        }
                    )
                )
        );
    }

    public BusinessFunc getFunc(final Class<?> funcClazz) {
        return funcContainer.get(funcClazz);
    }

    public class BusinessFunc {

        public final Class<?> funcClazz;

        public final DynamicFunc<?> func;

        public final Func<CallerParameter,Set<BusinessFuncCallable.Args>> paramFitFunc;

        public BusinessFunc(Class<?> funClass, DynamicFunc<?> func, 
            Func<CallerParameter,Set<BusinessFuncCallable.Args>> paramFitFunc) {
            this.funcClazz = funClass;
            this.func = func;
            this.paramFitFunc = paramFitFunc;
        }
    }

}
