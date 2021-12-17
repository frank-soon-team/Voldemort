package com.fs.voldemort.business.paramfinder;

import com.fs.voldemort.business.paramfinder.util.ConstantPoolUtil;
import com.fs.voldemort.business.paramfinder.util.JavassistUtil;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * The parameter finder for lambda
 */
public class LambdaParamFinder implements ParamFinder{

    @Override
    public <T> Collection<ParamNameTypeFindResult> getParam(T target) throws ParamFinderException{

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
    }
}
