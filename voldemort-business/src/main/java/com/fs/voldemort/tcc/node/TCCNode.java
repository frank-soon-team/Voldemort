package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.tcc.state.TCCStatus;

public class TCCNode extends CallerNode {

    private final ITCCHandler tccHandler;
    private TCCNodeParameter nodeParameter;
    private TCCStatus status;

    public TCCNode(ITCCHandler tccHandler) {
        this(tccHandler, TCCStatus.Initail);
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
        tccHandler.confirm(nodeParameter.getTCCState());
    }

    public void doCancel() {
        if(nodeParameter == null) {
            throw new IllegalStateException("the nodeParameter is null.");
        }
        tccHandler.cancel(nodeParameter.getTCCState());
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

    public String getName() {
        if(tccHandler instanceof BaseTCCHandler) {
            return ((BaseTCCHandler) tccHandler).getName();
        }
        return "";
    }

    
}
