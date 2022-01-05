package com.fs.voldemort.business.fit;

import java.lang.annotation.*;

@Fit
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
public @interface AutoFit {
    FitArg f_getArg = (paramFindResult, fitContext) -> {
        Object result = fitContext.getContext(paramFindResult.getParamName());
        return result != null ? result : fitContext.getIocInstance(paramFindResult.getParamClazz(),paramFindResult.getParamName());
    };
}
