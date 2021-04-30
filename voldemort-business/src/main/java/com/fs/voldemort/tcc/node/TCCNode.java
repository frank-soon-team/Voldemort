package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;

public class TCCNode extends CallerNode {

    private final ITCCHandler tccHandler;
    private CallerParameter nodeParameter;

    public TCCNode(ITCCHandler tccHandler) {
        // TODO caller parameter 参数对接
        super(p -> tccHandler.goTry());
        this.tccHandler = tccHandler;
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
}
