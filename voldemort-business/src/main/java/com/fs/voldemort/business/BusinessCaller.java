package com.fs.voldemort.business;

import com.fs.voldemort.business.exception.BusinessFuncInitializationException;
import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

import java.util.Set;

/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends BusinessFuncAvailableCaller {

    private BusinessCaller() {
        super();
    }

    public BusinessCaller call(Class<?> funcClazz) {
        if(getFunc == null) {
            throw new BusinessFuncInitializationException("The businessFuncAvailableCaller initialize error, " +
                    "please check component config!");
        }

        final BusinessFunc bFunc = getFunc.call(funcClazz);
        if(bFunc == null) {
            throw new BusinessFuncInitializationException("Can not find func, please ensure funcClazz:"+
                    funcClazz.getName() + "has been config Correctly...");
        }

        this.call(p -> {
            Set<BusinessFuncCallable.Arg> result = bFunc.paramFitFunc.call(p);
            return bFunc.func.call(result.toArray());
        });
        return this;
    }

    @Override
    public BusinessCaller call(Func1<CallerParameter, Object> func) {
        super.call(func);
        return this;
    }

    public static BusinessCaller create() {
        return new BusinessCaller();
    }

}
