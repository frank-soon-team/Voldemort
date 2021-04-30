package com.fs.voldemort.tcc.exception;

public class TCCStateException extends IllegalStateException {
    
    public TCCStateException(String message) {
        super(message);
    }

    public TCCStateException(String message, Throwable e) {
        super(message, e);
    }

}
