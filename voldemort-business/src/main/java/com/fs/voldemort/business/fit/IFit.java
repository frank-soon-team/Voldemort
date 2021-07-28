package com.fs.voldemort.business.fit;

import com.fs.voldemort.core.support.CallerParameter;

import java.lang.reflect.Method;

public interface IFit {
    default Object[] fit(Method method, CallerParameter param) {return new Object[0];}
}
