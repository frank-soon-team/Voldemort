package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFuncMark;
import com.fs.voldemort.core.exception.CallerException;
import com.fs.voldemort.func.DynamicFunc;
import com.fs.voldemort.func.Func;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public class BusinessFuncRegistry {

    private Map<Class, DynamicFunc> funcRegistry ;

    private final Func<Class,Map<String, Object>> getBusinessFuncHorcruxesFunc;

    public BusinessFuncRegistry(Func<Class,Map<String, Object>> getBusinessFuncHorcruxesFunc){
        this.getBusinessFuncHorcruxesFunc = getBusinessFuncHorcruxesFunc;
        init();
        scanAndFill();
    }

    private void init(){
        funcRegistry = new ConcurrentHashMap<>();
        BusinessCaller.businessFuncRegistry = this;
    }

    private void scanAndFill() {
        final Map<String, Object> funcHorcruxesBeanMap = getBusinessFuncHorcruxesFunc.call(BusinessFuncHorcruxes.class);
        if (funcHorcruxesBeanMap.isEmpty()) {
            return;
        }
        final Map<Method, Object> assistFuncHorcruxesInstanceMap = new HashMap<>(funcRegistry.size());
        funcRegistry.putAll(funcHorcruxesBeanMap.values().stream()
                .map(funcHorcruxes -> Arrays.stream(funcHorcruxes.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(BusinessFuncMark.class))
                        .peek(method -> assistFuncHorcruxesInstanceMap.put(method, funcHorcruxes))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        method -> method.getAnnotation(BusinessFuncMark.class).value(),
                        method -> args -> {
                                try {
                                    return method.invoke(assistFuncHorcruxesInstanceMap.get(method), args);
                                } catch (Exception e) {
                                    throw new CallerException(e.getMessage());
                                }
                            }
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    public <T> DynamicFunc<T> getFunc(final Class funcClazz, Class<T> resultClazz) {
        return funcRegistry.get(funcClazz);
    }

}
