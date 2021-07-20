package com.fs.voldemort.tcc.exception;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;

public class ExecuteCallerNodeException extends IllegalStateException {

    private CallerParameter parameter;

    private CallerNode node;

    private String name;

    public ExecuteCallerNodeException(Throwable e, CallerNode node, CallerParameter parameter) {
        this(e, node, null, parameter);
    }

    public ExecuteCallerNodeException(Throwable e, CallerNode node, String name, CallerParameter parameter) {
        super(e);
        this.node = node;
        this.parameter = parameter;
    }

    public CallerNode getNode() {
        return node;
    }

    public CallerParameter getParameter() {
        return parameter;
    }

    public String getName() {
        return name;
    }
    
}
