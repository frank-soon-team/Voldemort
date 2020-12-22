package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.func.Func;
/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends Caller {

    public static BusinessFuncRegistry businessFuncRegistry;

    public static BusinessCaller create(){
        return new BusinessCaller();
    }

    public BusinessCaller call(Class<BusinessFuncCallable> funcClazz) {

        // this.call(p -> null);

        // 1. 获取bean businessFuncRegistry.get(funcClazz);
        // 2. 找到要执行的函数 Method
        // 3. 得到Method参数列表

        // 4. 准备参数，如果bean instanceof BusinessFuncCallable 调用paramFit，否则报错
        // 5. ParameterList<Args>.invoke(method);

        return this;

    }

    @Override
    public BusinessCaller call(Func<CallerParameter, Object> func) {
        super.call(func);
        return this;
    }

}
