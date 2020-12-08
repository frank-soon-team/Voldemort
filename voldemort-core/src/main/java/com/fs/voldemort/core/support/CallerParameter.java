package com.fs.voldemort.core.support;

public class CallerParameter {

    public final Object result;
    private final CallerContext context;
    private final ValueBag valueBag;

    protected CallerParameter() {
        this.valueBag = null;
        this.result = null;
        this.context = new CallerContext();
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

    private static class EmptyCallerParameter extends CallerParameter {}
    public final static CallerParameter Empty = new EmptyCallerParameter();
    public static boolean isEmpty(CallerParameter parameter) {
        return parameter == Empty;
    }
}
