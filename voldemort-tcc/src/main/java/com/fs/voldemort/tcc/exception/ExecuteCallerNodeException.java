package com.fs.voldemort.tcc.exception;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.tcc.node.TCCNode;

public class ExecuteCallerNodeException extends IllegalStateException {

    private String stage;

    private String statusName;

    private String nodeName;

    public ExecuteCallerNodeException(Throwable e, CallerNode node) {
        super(e);
        if(node instanceof TCCNode) {
            TCCNode tccNode = (TCCNode) node;
            this.nodeName = tccNode.getName();
            this.stage = tccNode.getStatus().getStage();
            this.statusName = tccNode.getStatus().name();
        }
    }

    public String getName() {
        return nodeName == null ? "CallerNode" : "TCCNode - " + nodeName;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder(getName());
        if(stage != null) {
            builder.append("[").append(stage).append("]");
        }
        if(statusName != null) {
            builder.append("[").append(statusName).append("]");
        }
        builder.append(", ").append(super.getMessage());

        return builder.toString();
    }
    
}
