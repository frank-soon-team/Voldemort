package com.fs.voldemort.tcc.exception;

import com.fs.voldemort.core.support.CallerNode;

public class TCCTimeoutException extends ExecuteCallerNodeException {

    public TCCTimeoutException(Throwable e, CallerNode node) {
        super(e, node);
    }
    
}
