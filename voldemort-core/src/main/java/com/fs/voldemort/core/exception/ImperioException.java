package com.fs.voldemort.core.exception;

public class ImperioException extends RuntimeException {

    private static final long serialVersionUID = -649953252877047554L;

    public ImperioException(String message) {
        super(message);
    }

    public ImperioException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getLocalizedMessage() {
        return "夺魂咒";
    }
    
}
