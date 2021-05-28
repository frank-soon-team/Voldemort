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
    default Object[] paramFit(@NonNull final CallerParameter p) {

        final List<Method> funcMethodList = Arrays.stream(getClass().getDeclaredMethods())
            .filter(method -> Arrays.stream(method.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation.annotationType().equals(BusinessFunc.class)))
            .collect(Collectors.toList());

        if(funcMethodList.size() > 1) {
            throw new CallerException("Settle function can only have one func method!");
        }else if(funcMethodList.isEmpty()) {
            return new Object[0];
        }

        final Method funcMethod = funcMethodList.get(0);

        final Set<Arg> argSet = Arrays.stream(funcMethod.getParameters()).map(param-> new Arg(param.getName())).collect(Collectors.toSet());

        if(!argSet.isEmpty()) {
            //Deal result
            final Arg resultArg = argSet.iterator().next();
            resultArg.value = p.result;

            //Deal context arg
            if(argSet.size()>1) {
                argSet.stream().skip(1).forEach(arg->arg.value = p.context().get(arg.name));
            }
            return argSet.stream().map(arg->arg.value).toArray();
        }
        return new Object[0];
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
