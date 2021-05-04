package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BusinessFunc;

public interface BusinessFuncAddable {

    void add(Class<?> funcClazz, BusinessFunc func);

}
