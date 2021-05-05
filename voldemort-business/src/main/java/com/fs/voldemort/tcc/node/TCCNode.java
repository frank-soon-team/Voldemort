package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.state.TCCStatus;

public class TCCNode extends CallerNode {

    private final ITCCHandler tccHandler;
    private CallerParameter nodeParameter;
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
        
        // TODO caller parameter 参数对接
        tccHandler.confirm();
    }

    public void doCancel() {
        if(nodeParameter == null) {
            throw new IllegalStateException("the nodeParameter is null.");
        }
        
        // TODO caller parameter 参数对接
        tccHandler.cancel();
    }
    
    public void setNodeParameter(CallerParameter parameter) {
        this.nodeParameter = parameter;
    }
    
    public CallerParameter getNodeParameter() {
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

    
}
