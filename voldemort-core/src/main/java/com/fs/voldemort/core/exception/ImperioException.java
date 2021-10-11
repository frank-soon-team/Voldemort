package com.fs.voldemort.core.exception;

// 一般用于包裹其他异常
public class ImperioException extends IllegalStateException {

    public ImperioException(String message) {
        super(message);
    }

    public ImperioException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getLocalizedMessage() {
        return "【夺魂咒】 > " + getMessage();
    }
    
}
