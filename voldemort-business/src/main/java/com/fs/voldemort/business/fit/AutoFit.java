package com.fs.voldemort.business.fit;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
public @interface AutoFit {
    FitArg f_getArg = (clazz, name, fitContext) -> {
        Object result = fitContext.getContext(name);
        return result != null ? result : fitContext.getBean(clazz,name);
    };
}
