package com.fs.voldemort.business.paramfinder;

import com.fs.voldemort.business.paramfinder.util.ConstantPoolUtil;
import com.fs.voldemort.business.paramfinder.util.JavassistUtil;

import java.lang.reflect.Method;

public class ParamFinderLibrary {

    public static final ParamFinder<Method> methodParamFinder = target -> {
        if(target == null) {
            throw new ParamFinderException("The target of MethodParamFinder can not be null!");
        }

        if(!(target instanceof Method)) {
            throw new ParamFinderException("The target[" + target.getClass().getName() + "] of MethodParamFinder " +
                    "that only support java.lang.reflect.Method.class!");
        }

        try {
            return JavassistUtil.getParam(target);
        } catch (Exception e) {
            throw new ParamFinderException(e);
        }
    };

    public static final ParamFinder<Object> lambdaParamFinder = target -> {
        if(target == null) {
            throw new ParamFinderException("The target of LambdaParamFinder can not be null!");
        }

        if (target.getClass().isSynthetic()) {
            try {
                Method method = ConstantPoolUtil.getParameterTypeAndName(target);
                return JavassistUtil.getParam(method);
            } catch (Exception e) {
                throw new ParamFinderException(e);
            }
        }
        throw new ParamFinderException("Currently only synthetic functions has been already supported!");
    };

}
