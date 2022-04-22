package com.fs.voldemort.tcc.state;

public enum TCCStatus {

    TryPending(11),
    TrySuccess(12),
    TryFaild(13),
    TryTimeout(14),
    
    ConfirmPending(21),
    ConfirmSuccess(22),
    ConfirmFailed(23),
    ConfirmTimeout(24),

    CancelPending(31),
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

    public String getPhase() {
        if(value >= 10 && value <= 20) {
            return TCCPhase.TRY;
        } else if (value >= 20 && value <= 30) {
            return TCCPhase.CONFIRM;
        } else {
            return TCCPhase.CANCEL;
        }
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
