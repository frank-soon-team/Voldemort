package com.fs.voldemort.business.fit;

public class FitException extends RuntimeException{
    public FitException() {
    }

    public FitException(String message) {
        super(message);
    }

    public FitException(String message, Throwable cause) {
        super(message, cause);
    }

    public FitException(Throwable cause) {
        super(cause);
    }

    public FitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
