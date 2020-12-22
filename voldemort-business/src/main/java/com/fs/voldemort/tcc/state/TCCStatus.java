package com.fs.voldemort.tcc.state;

public enum TCCStatus {

    Initail(1),
    Success(2),
    Failed(3),

    TrySuccess(12),
    TryFaild(13),
    
    ConfirmSuccess(22),
    ConfirmFailed(23),
    ConfirmTimeout(24),

    CancelSuccess(32),
    CancelFailed(33),
    CancelTimeout(34);

    private final int status;

    private TCCStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    
}
