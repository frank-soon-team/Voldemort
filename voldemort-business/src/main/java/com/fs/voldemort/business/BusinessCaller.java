package com.fs.voldemort.business;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.func.Func;
/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends Caller {

    public static BusinessFuncContainer businessFuncContainer;

    private BusinessCaller() {
        super();
    }

    public BusinessCaller call(Class<?> funcClazz) {
        final BusinessFunc bFunc = businessFuncContainer.getFunc(funcClazz);
        this.call(p -> {
            return bFunc.func.call(bFunc.paramFitFunc.call(p).toArray());
        });
        return this;
    }

    @Override
    public BusinessCaller call(Func<CallerParameter, Object> func) {
        super.call(func);
        return this;
    }

    public static BusinessCaller create() {
        return new BusinessCaller();
    }

}
