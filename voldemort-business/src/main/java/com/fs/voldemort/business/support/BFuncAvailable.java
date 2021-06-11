package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BFunc;

public interface BFuncAvailable {

    BFunc getFunc(final Class<?> funcClazz);

}
