package com.fs.voldemort.tcc;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNodeParameter;

public class TCCCaller extends Caller<Void> {

    public TCCCaller(TCCManager tccManager) {
        super(tccManager);
    }

    public TCCCaller(TCCManager tccManager, Object param) {
        super(tccManager);
        call(p -> {
            // TODO：这里可能会导致补偿场景下的 ITCCState中的Param属性被覆盖
            ((TCCNodeParameter) p).getTCCState().setParam(param);
            return null;
        });
    }

    public TCCCaller call(ITCCHandler tccHandler) {
        getTCCManager().add(tccHandler);
        return this;
    }

    public TCCManager getTCCManager() {
        return (TCCManager) this.funcList;
    }
    
}
