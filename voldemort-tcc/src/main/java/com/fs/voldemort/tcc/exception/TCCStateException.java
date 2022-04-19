package com.fs.voldemort.tcc.exception;

public class TCCStateException extends IllegalStateException {

    private final String expectationStatusName;
    private final String errorStatusName;
    
    public TCCStateException(String message, String expectationStatusName, String errorStatusName) {
        super(message + String.format(", the expectation status value is [%s], but current status value is [%s].", expectationStatusName, errorStatusName));
        this.expectationStatusName = expectationStatusName;
        this.errorStatusName = errorStatusName;
    }

    public String getExpectationStatusName() {
        return expectationStatusName;
    }

    public String getErrorStatusName() {
        return errorStatusName;
    }

}
