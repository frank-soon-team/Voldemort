package com.fs.voldemort.business.paramfinder;

import com.fs.voldemort.business.paramfinder.util.JavassistUtil;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * The parameter finder for method
 */
public class MethodParamFinder implements ParamFinder{
    @Override
    public <T> Collection<ParamNameTypeFindResult> getParam(T target) throws ParamFinderException{

        if(target == null) {
            throw new ParamFinderException("The target of MethodParamFinder can not be null!");
        }

        if(!(target instanceof Method)) {
            throw new ParamFinderException("The target[" + target.getClass().getName() + "] of MethodParamFinder " +
                    "that only support java.lang.reflect.Method.class!");
        }

        try {
            return JavassistUtil.getParam((Method) target);
        } catch (Exception e) {
            throw new ParamFinderException(e);
        }
    }
}
