package com.fs.business.support;

import com.fs.core.caller.exception.CallerException;
import com.fs.core.caller.support.Param;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public interface BusinessFuncCallable {

    @SuppressWarnings("unchecked")
    default Set<Args> paramFit(@NonNull final Param p) {

        final List<Method> funcMethodList = Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(BusinessFuncMark.class)))
                .collect(Collectors.toList());

        if(funcMethodList.size()>1) {
            throw new CallerException("Settle function can only have one func method!");
        }else if(funcMethodList.isEmpty()) {
            return new HashSet<>();
        }

        final Set<Args> argsSet = new HashSet<>();

        final Method funcMethod = funcMethodList.get(0);
        Arrays.stream(funcMethod.getParameters()).forEach(param->{
            final String paramName = param.getName();
            argsSet.add(new Args(paramName));
        });

        //Get Param.result field collection
        Object result = p.result;
        if(null != result) {
            //Check result is fundamental type
            final Class resultClazz = result.getClass();
            if(!isAssignableFromMulti(resultClazz,Number.class,CharSequence.class,Collection.class,Map.class)) {
                final Field[] resultFields = result.getClass().getDeclaredFields();
                final Map<String,Object> resultFieldMap = Arrays.stream(resultFields)
                        .collect(Collectors.toMap(Field::getName,field -> {
                            boolean isAccessible = field.isAccessible();
                            if(!isAccessible) {
                                field.setAccessible(true);
                            }
                            try {
                                return field.get(result);
                            } catch (IllegalAccessException e) {
                                throw new CallerException("SettleFuncCallable paramFit error:"+e.getMessage());
                            }finally {
                                if(!isAccessible) {
                                    field.setAccessible(false);
                                }
                            }
                        }));

                return argsSet.stream().peek(arg-> arg.value = resultFieldMap.get(arg.name))
                        .filter(arg-> arg.value != null)
                        .collect(Collectors.toSet());
            }
        }

        if(!p.context.isEmpty()) {


        }


        //get Param.context mapper field collection



        return null;



    }

    static boolean isAssignableFromMulti(Class testClazz, Class... clazzes) {
        if(clazzes.length > 0){
            for(Class clazz : clazzes) {
                if(testClazz.isAssignableFrom(clazz)) {
                    return true;
                }
            }
        }
        return false;
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
            Args args = (Args) o;
            return name.equals(args.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}
