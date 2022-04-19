package com.fs.voldemort.tcc.exception;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.node.TCCNode;

public class ExecuteCallerNodeException extends IllegalStateException {

    private CallerParameter parameter;

    private CallerNode node;

    private String nodeName;

    public ExecuteCallerNodeException(Throwable e, CallerNode node) {
        this(e, node, null);
    }

    public ExecuteCallerNodeException(Throwable e, CallerNode node, CallerParameter parameter) {
        super(e);
        this.node = node;
        this.parameter = parameter;
        if(node instanceof TCCNode) {
            this.nodeName = ((TCCNode) node).getName();
        }
    }

    public CallerNode getNode() {
        return node;
    }

    public CallerParameter getParameter() {
        return parameter;
    }

    public String getName() {
        return nodeName == null ? "CallerNode" : nodeName;
    }

    @Override
    public String getMessage() {
        if(node instanceof TCCNode) {
            TCCNode tccNode = (TCCNode) node;
            return String.format("[%s][%s][%d], %s", 
                getName(), 
                tccNode.getStatus().getStage(), 
                tccNode.getStatus().getValue(), 
                super.getMessage());
        }
        return super.getMessage();
    }
    
}
