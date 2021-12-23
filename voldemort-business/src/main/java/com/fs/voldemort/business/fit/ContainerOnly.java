package com.fs.voldemort.business.fit;

import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.functional.func.Func3;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
public @interface ContainerOnly{
    FitArg f_getArg = (clazz, name, fitContext) -> fitContext.getContext(name);
}
