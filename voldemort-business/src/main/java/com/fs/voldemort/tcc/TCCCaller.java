package com.fs.voldemort.tcc;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.tcc.node.ITCCHandler;

public class TCCCaller extends Caller {

    public TCCCaller(TCCManager tccManager) {
        super(tccManager);
    }

    public Caller call(ITCCHandler tccHandler) {
        getTCCManager().add(tccHandler);
        return this;
    }

    public TCCManager getTCCManager() {
        return (TCCManager) this.funcList;
    }

    public static TCCCaller create(TCCManager tccManager) {
        if(tccManager == null) {
            throw new IllegalArgumentException("the parameter tccManager is required. ");
        }
        return new TCCCaller(tccManager);
    }
    
}
