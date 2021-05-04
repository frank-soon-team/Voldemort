package com.fs.voldemort.business.support;

import com.fs.voldemort.business.BusinessFunc;

public interface BusinessFuncAvailable {

    BusinessFunc getFunc(final Class<?> funcClazz);

}
