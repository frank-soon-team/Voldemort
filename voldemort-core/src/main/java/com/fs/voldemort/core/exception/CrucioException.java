package com.fs.voldemort.core.exception;

// 用于校验性异常，通常主动抛出
public class CrucioException extends IllegalArgumentException {

    public CrucioException(String message) {
        super(message);
    }

    public CrucioException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getLocalizedMessage() {
        return "【钻心咒】 > " + getMessage();
    }
    
}
