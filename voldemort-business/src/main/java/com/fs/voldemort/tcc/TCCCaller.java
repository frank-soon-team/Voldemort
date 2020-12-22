package com.fs.voldemort.tcc;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.tcc.node.ITCCHandler;

public class TCCCaller extends Caller {

    public TCCCaller() {
        super(new TCCManager());
    }

    public Caller call(ITCCHandler tccHandler) {
        getTCCManager().add(tccHandler);
        return this;
    }

    public TCCManager getTCCManager() {
        return (TCCManager) this.funcList;
    }
    
}
