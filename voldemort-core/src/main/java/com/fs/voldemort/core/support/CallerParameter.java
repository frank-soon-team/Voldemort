package com.fs.voldemort.core.support;

public class CallerParameter {

    public final Object result;
    private final CallerContext context;
    private final ValueBag valueBag;

    public CallerParameter(CallerParameter parameter) {
        if(parameter == null) {
            throw new IllegalArgumentException("the parameter is required.");
        }
        this.valueBag = parameter.valueBag;
        this.result = parameter.result;
        this.context = new CallerContext(parameter.context());
    }

    public CallerParameter(Object result, CallerContext context) {
        this.result = result;
        this.valueBag = new ValueBag();
        this.context = context;
    }
    
    public CallerContext context() {
        return context;
    }

    public Object getValue(String parameterName) {
        if(parameterName == null || parameterName.length() == 0) {
            throw new IllegalArgumentException("the parameter parameterName is required.");
        }

        // 从Parameter中获取
        Object value = valueBag.get(parameterName);

        if(value == null) {
            // 从Context里面获取
            value = context().get(parameterName);
        }

        return value;
    }
}
