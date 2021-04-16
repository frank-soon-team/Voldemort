package com.fs.voldemort.business;

import com.fs.voldemort.business.BusinessFuncRegistry.BusinessFunc;
import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends Caller {

    public static BusinessFuncRegistry businessFuncRegistry;

    private BusinessCaller() {
        super();
    }

    public BusinessCaller call(Class<BusinessFuncCallable> funcClazz) {
        final BusinessFunc bFunc = businessFuncRegistry.getFunc(funcClazz);
        this.call(p -> {
            return bFunc.func.call(bFunc.paramFitFunc.call(p).toArray());
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
