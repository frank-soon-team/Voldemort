package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BFunc;

public interface BFuncAddable {

    void addFunc(Class<?> funcClazz, BFunc func);

}
