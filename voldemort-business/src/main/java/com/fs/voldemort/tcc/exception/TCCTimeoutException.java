package com.fs.voldemort.tcc.exception;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;

public class TCCTimeoutException extends ExecuteCallerNodeException {

    private static final long serialVersionUID = 1L;

    public TCCTimeoutException(Throwable e, CallerNode node, CallerParameter parameter) {
        super(e, node, parameter);
    }
    
}
