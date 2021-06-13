package com.fs.voldemort.business;

import com.fs.voldemort.business.exception.BusinessFuncInitializationException;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;

/**
 * Polymerize caller
 *
 * @author frank
 */
public class BFuncCaller extends BFuncAvailableCaller {

    private BFuncCaller() {
        super();
    }

    public BFuncCaller call(Class<?> funcClazz) {
        if(getFunc == null) {
            throw new BusinessFuncInitializationException("The businessFuncAvailableCaller initialize error, " +
                    "please check component config!");
        }

        final BFunc bFunc = getFunc.call(funcClazz);
        if(bFunc == null) {
            throw new BusinessFuncInitializationException("Can not find func, please ensure funcClazz:"+
                    funcClazz.getName() + "has been config Correctly...");
        }

        this.call(p -> {
            Object[] result = bFunc.paramFitFunc.call(p);
            return bFunc.func.call(result);
        });
        return this;
    }

    @Override
    public BFuncCaller call(Func1<CallerParameter, Object> func) {
        super.call(func);
        return this;
    }

    public static BFuncCaller create() {
        return new BFuncCaller();
    }

}
