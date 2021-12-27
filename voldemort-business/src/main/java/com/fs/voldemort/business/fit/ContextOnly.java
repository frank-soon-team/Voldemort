package com.fs.voldemort.business.fit;

import java.lang.annotation.*;

@Fit
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
public @interface ContextOnly {
    FitArg f_getArg = (paramFindResult, fitContext) -> fitContext.getBean(paramFindResult.getParamClazz(), paramFindResult.getParamName());
}
