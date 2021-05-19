package com.fs.voldemort.business.support;

import com.fs.voldemort.core.exception.CallerException;
import com.fs.voldemort.core.support.CallerParameter;
import lombok.NonNull;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public interface BusinessFuncCallable {

    default Set<Args> paramFit(@NonNull final CallerParameter p) {

        final List<Method> funcMethodList = Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(BusinessFuncMark.class)))
                .collect(Collectors.toList());

        if(funcMethodList.size() > 1) {
            throw new CallerException("Settle function can only have one func method!");
        }else if(funcMethodList.isEmpty()) {
            return new HashSet<>();
        }

        Set<Args> argsSet = new HashSet<>();

        final var funcMethod = funcMethodList.get(0);
        Arrays.stream(funcMethod.getParameters()).forEach(param->{
            final String paramName = param.getName();
            argsSet.add(new Args(paramName));
        });

        final Map<String,Object> resultFieldMap = new HashMap<>(argsSet.size());

        //Get args from param.result fields
        Object result = p.result;
        if(null != result) {
            //Check result is fundamental type
            final Class<?> resultClazz = result.getClass();
            if(!isAssignableFromMulti(resultClazz,Number.class,CharSequence.class,Collection.class,Map.class)) {
                final Field[] resultFields = result.getClass().getDeclaredFields();
                resultFieldMap
                    .putAll(Arrays.stream(resultFields)
                    .collect(
                        Collectors.toMap(Field::getName, field -> {
                            boolean isAccessible = field.canAccess(result);
                            if(!isAccessible) {
                                field.setAccessible(true);
                            }
                            try {
                                return field.get(result);
                            } catch (IllegalAccessException e) {
                                throw new CallerException("SettleFuncCallable paramFit error:"+e.getMessage());
                            } finally {
                                field.setAccessible(isAccessible);
                            }
                        })
                    )
                );
            }
        }

        //Get args from context
        return 
            argsSet.stream()
                .filter(arg -> {
                    Object value = resultFieldMap.get(arg.name);
                    if(value != null) {
                        arg.value = value;
                    } else {
                        arg.value = p.context().get(arg.name);
                    }
                    return arg.value != null;
                })
                .collect(Collectors.toSet());
    }

    static boolean isAssignableFromMulti(Class<?> testClazz, Class<?>... clazzes) {
        if(clazzes.length > 0){
            for(Class<?> clazz : clazzes) {
                if(testClazz.isAssignableFrom(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {

        Reflections reflections = new Reflections(String.class);
        reflections.getAllTypes()


        System.out.println(BusinessFuncCallable.isAssignableFromMulti(String.class,CharSequence.class));
    }

    class Args{
        public final String name;
        public Object value;

        public Args(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            var args = (Args) o;
            return name.equals(args.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}
