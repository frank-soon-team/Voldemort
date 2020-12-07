package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.support.Param;
import com.fs.voldemort.func.Func;

import lombok.NonNull;

/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends Caller {

    public static BusinessFuncRegistry businessFuncRegistry;

    public static BusinessCaller create(){
        BusinessCaller caller = new BusinessCaller();
        caller.callFunc = r -> new Param<>(null, caller.context);
        return caller;
    }

    public BusinessCaller call(Class<BusinessFuncCallable> funcClazz) {

        this.call(p -> null);

        // 1. 获取bean businessFuncRegistry.get(funcClazz);
        // 2. 找到要执行的函数 Method
        // 3. 得到Method参数列表
        // 4. 准备参数，如果bean instanceof BusinessFuncCallable 调用paramFit，否则报错
        // 5. ParameterList<Args>.invoke(method);

        return this;

    }

    @Override
    public <T,R> Caller call(@NonNull Func<Param<T>,R> callFunc) {
        super.call(callFunc);
        return this;
    }

}
