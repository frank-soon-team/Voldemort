package com.fs.voldemort.tcc.state;

public enum TCCStatus {

    Initail(1),

    TrySuccess(12),
    TryFaild(13),
    TryTimeout(14),
    
    ConfirmSuccess(22),
    ConfirmFailed(23),
    ConfirmTimeout(24),

    CancelSuccess(32),
    CancelFailed(33),
    CancelTimeout(34);

    private final int value;

    private TCCStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TCCStatus valueOf(int value) {
        TCCStatus[] values = values();
        for(int i = 0; i < values.length; i++) {
            TCCStatus status = values[i];
            if(status.value == value) {
                return status;
            }
        }

        throw new IllegalArgumentException("value: " + value + " is invalid");
    }
    
}
