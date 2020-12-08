package com.fs.voldemort.core.exception;

public class CrucioException extends IllegalStateException {

    private static final long serialVersionUID = -7751387277203980437L;

    public CrucioException(String message) {
        super(message);
    }

    public CrucioException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getLocalizedMessage() {
        return "钻心咒";
    }
    
}
