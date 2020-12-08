package com.fs.voldemort.core.exception;

public class AvadaKedavraException extends Exception {

    private static final long serialVersionUID = -1740490685999402263L;

    public AvadaKedavraException(String message) {
        super(message);
    }

    public AvadaKedavraException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getLocalizedMessage() {
        return "阿瓦达索命";
    }
    
}
