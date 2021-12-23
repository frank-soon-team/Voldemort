package com.fs.voldemort.business.fit;

import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerParameter;

public class FitContext {

    private final CallerParameter callerParameter;

    private final Func2<Class, String, ?> getBean;

    public FitContext(CallerParameter callerParameter, Func2<Class, String, ?> getBean) {
        this.callerParameter = callerParameter;
        this.getBean = getBean;
    }

    public CallerParameter callerParameter() {
        return callerParameter;
    }

    public CallerContext callerContext() {
        return callerParameter.context();
    }

    public Object getContext(String key) {
        return callerContext().get(key);
    }

    public Object getBean(Class beanClazz, String beanName) {
        return getBean.call(beanClazz, beanName);
    }
}