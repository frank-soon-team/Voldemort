package com.fs.voldemort.business.paramfinder;

public class ParamFinderException extends RuntimeException{

    public ParamFinderException() {
    }

    public ParamFinderException(String message) {
        super(message);
    }

    public ParamFinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamFinderException(Throwable cause) {
        super(cause);
    }

    public ParamFinderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
