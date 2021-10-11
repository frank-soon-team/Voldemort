package com.fs.voldemort.core.exception;

// 致命异常，主动中断执行时使用
public class AvadaKedavraException extends RuntimeException {

    public AvadaKedavraException(String message) {
        super(message);
    }

    public AvadaKedavraException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getLocalizedMessage() {
        return "【阿瓦达索命】 > " + getMessage();
    }
    
}
