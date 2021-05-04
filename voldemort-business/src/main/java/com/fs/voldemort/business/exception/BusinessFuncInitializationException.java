package com.fs.voldemort.business.exception;

/**
 * @author frank
 */
public class BusinessFuncInitializationException extends RuntimeException{

    public BusinessFuncInitializationException(String message) {
        super(message);
    }

    public BusinessFuncInitializationException(String message, Throwable e) {
        super(message, e);
    }

}
