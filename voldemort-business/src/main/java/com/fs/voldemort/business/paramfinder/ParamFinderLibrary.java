package com.fs.voldemort.business.paramfinder;

import com.fs.voldemort.business.paramfinder.util.ConstantPoolUtil;
import com.fs.voldemort.business.paramfinder.util.JavassistUtil;

import java.lang.reflect.Method;

public class ParamFinderLibrary {

    public static final ParamFinder<Method> f_MethodParamFinder = target -> {
        if(target == null) {
            throw new ParamFinderException("The target of MethodParamFinder can not be null!");
        }

        try {
            return JavassistUtil.getParam(target);
        } catch (Exception e) {
            throw new ParamFinderException(e);
        }
    };

    public static final ParamFinder<Object> f_LambdaParamFinder = target -> {
        if(target == null) {
            throw new ParamFinderException("The target of LambdaParamFinder can not be null!");
        }

        if (target.getClass().isSynthetic()) {
            try {
                return JavassistUtil.getParam(ConstantPoolUtil.getRealityMethod(target));
            } catch (Exception e) {
                throw new ParamFinderException(e);
            }
        }
        throw new ParamFinderException("Currently only synthetic functions has been already supported!");
    };

}
