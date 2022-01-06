package com.fs.voldemort.business.fit;

import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

public interface IGetArg {
    Func1<CallerParameter,?> getArgs();
}
