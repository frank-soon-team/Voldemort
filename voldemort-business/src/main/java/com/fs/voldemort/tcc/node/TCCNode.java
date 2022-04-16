package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.state.TCCStatus;

public class TCCNode extends CallerNode {

    private final ITCCHandler tccHandler;
    private TCCNodeParameter nodeParameter;
    private TCCStatus status;
    private ExecuteCallerNodeException error;

    public TCCNode(ITCCHandler tccHandler) {
        this(tccHandler, TCCStatus.TryPending);
    }

    public TCCNode(ITCCHandler tccHandler, TCCStatus status) {
        super(p -> tccHandler.goTry(p));
        this.tccHandler = tccHandler;
        this.status = status;
    }

    public void doConfirm() {
        if(nodeParameter == null) {
            throw new IllegalStateException("the nodeParameter is null.");
        }
        tccHandler.confirm(nodeParameter);
    }

    public void doCancel() {
        if(nodeParameter == null) {
            throw new IllegalStateException("the nodeParameter is null.");
        }
        tccHandler.cancel(nodeParameter);
    }
    
    public void setNodeParameter(TCCNodeParameter parameter) {
        this.nodeParameter = parameter;
    }
    
    public TCCNodeParameter getNodeParameter() {
        return this.nodeParameter;
    }

    public ITCCHandler getTCCHandler() {
        return tccHandler;
    }

    public TCCStatus getStatus() {
        return status;
    }

    public void setStatus(TCCStatus status) {
        this.status = status;
    }

    public ExecuteCallerNodeException getError() {
        return error;
    }

    public void setError(ExecuteCallerNodeException error) {
        this.error = error;
    }

    public String getName() {
        if(tccHandler instanceof BaseTCCHandler) {
            return ((BaseTCCHandler) tccHandler).getName();
        }
        return "";
    }

    
}
