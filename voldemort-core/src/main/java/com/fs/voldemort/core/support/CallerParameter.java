package com.fs.voldemort.core.support;

public class CallerParameter {

    public final Object result;
    private final CallerContext context;

    public CallerParameter(CallerParameter callParameter) {
        if(callParameter == null) {
            throw new IllegalArgumentException("the parameter [callParameter] is required.");
        }
        this.result = callParameter.result;
        this.context = new CallerContext(callParameter.context().get());
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
