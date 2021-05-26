package com.fs.voldemort.business.support;

import com.fs.voldemort.core.exception.CallerException;
import com.fs.voldemort.core.support.CallerParameter;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frank
 */
public interface BusinessFuncCallable {

    String RESULT = "RES";

    /**
     * Get arg info of target function method, and adapt result that has been executed by last function,
     *
     * # Role 1
     *     Last func        call    Current func
     *     R func1()        --->    func2(R arg)
     * # Role 2
     *     Last func        call    Current func
     *     R func1()        --->    func2(R arg, C1 arg1, C2 arg2, C2 arg3)
     *  ## Parameter description
     *     R arg                    -> The result of last function
     *     C1 arg1  context param   -> The arg of context param
     *                                 Context param class is C1.class
     *                                 Context param key is     'arg1'
     *     C2 arg2                  -> Class: C1.class      key:'arg2'
     *     C2 arg3                  -> Class: C2.class      key:'arg3'
     */
    default Set<Arg> paramFit(@NonNull final CallerParameter p) {

        final List<Method> funcMethodList = Arrays.stream(getClass().getDeclaredMethods())
            .filter(method -> Arrays.stream(method.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation.annotationType().equals(BusinessFuncMark.class)))
            .collect(Collectors.toList());

        if(funcMethodList.size() > 1) {
            throw new CallerException("Settle function can only have one func method!");
        }else if(funcMethodList.isEmpty()) {
            return new HashSet<>();
        }

        final Method funcMethod = funcMethodList.get(0);

        final Set<Arg> argSet = Arrays.stream(funcMethod.getParameters()).map(param-> new Arg(param.getName())).collect(Collectors.toSet());

//        //Get args from context
//        if(argSet.size()>1){
//
//            return argSet.forEach()
//                    .filter(arg -> {
//                        Object value = resultFieldMap.get(arg.name);
//                        if(value != null) {
//                            arg.value = value;
//                        } else {
//                            arg.value = p.context().get(arg.name);
//                        }
//                        return arg.value != null;
//                    })
//                    .collect(Collectors.toSet());
//        }else if(argSet.size() == 1){
//            argSet.stream().findFirst().get().value = resultFieldMap.get(RESULT);
//            return argSet;
//        }else{
            return null;
//        }
    }

    static boolean isAssignableFromMulti(@NonNull final Object target,@NonNull final Class<?>... clazzes) {
        return clazzes.length > 0 && Arrays.stream(clazzes).anyMatch(clazz -> clazz.isInstance(target));
    }

    class Arg {
        public final String name;
        public Object value;

        public Arg(String name) {
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
            var args = (Arg) o;
            return name.equals(args.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}
