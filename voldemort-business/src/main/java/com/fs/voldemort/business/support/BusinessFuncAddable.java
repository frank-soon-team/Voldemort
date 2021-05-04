package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BusinessFunc;

public interface BusinessFuncAddable {

    void addFunc(Class<?> funcClazz, BusinessFunc func);

}
