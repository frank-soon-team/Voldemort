package com.fs.voldemort.tcc.exception;

public class TCCStateException extends IllegalStateException {

    private final int expectationStatusValue;
    private final int errorStatusValue;
    
    public TCCStateException(String message, int expectationStatusValue, int errorStatusValue) {
        super(message + String.format(", the expectation status value is [%d], but current status value is [%d].", expectationStatusValue, errorStatusValue));
        this.expectationStatusValue = expectationStatusValue;
        this.errorStatusValue = errorStatusValue;
    }

    public int getExpectationStatusValue() {
        return expectationStatusValue;
    }

    public int getErrorStatusValue() {
        return errorStatusValue;
    }

}
