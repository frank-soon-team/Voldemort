package com.fs.voldemort.business.fit;

public class FitInitException extends RuntimeException{
    public FitInitException() {
    }

    public FitInitException(String message) {
        super(message);
    }

    public FitInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public FitInitException(Throwable cause) {
        super(cause);
    }

    public FitInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
