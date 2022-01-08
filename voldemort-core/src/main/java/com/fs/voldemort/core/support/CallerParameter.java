package com.fs.voldemort.core.support;

public class CallerParameter {

    public final Object result;
    private final CallerContext context;

    public CallerParameter(CallerParameter parameter) {
        if(parameter == null) {
            throw new IllegalArgumentException("the parameter is required.");
        }
        this.result = parameter.result;
        this.context = new CallerContext(parameter.context().get());
    }

    public CallerParameter(Object result, CallerContext context) {
        this.result = result;
        this.context = context;
    }
    
    public CallerContext context() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public <R> R castResult() {
        return (R) result;
    }
}
