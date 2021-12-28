package com.fs.voldemort.business.fit;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
public @interface ContainerOnly{
    FitArg f_getArg = (paramFindResult, fitContext) -> fitContext.getContext(paramFindResult.getParamName());
}
