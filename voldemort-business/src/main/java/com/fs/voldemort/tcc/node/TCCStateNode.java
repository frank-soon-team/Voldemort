package com.fs.voldemort.tcc.node;

import com.fs.voldemort.tcc.state.TCCStatus;

public class TCCStateNode extends TCCNode {

    public TCCStateNode(String name, TCCStatus status) {
        super(new TCCHandlerWrapper(name), status);
    }

    public void setTCCHandler(ITCCHandler tccHandler) {
        ((TCCHandlerWrapper) getTCCHandler()).setTccHandler(tccHandler);
    }
    
}
