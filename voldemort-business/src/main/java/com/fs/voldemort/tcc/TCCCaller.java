package com.fs.voldemort.tcc;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNodeParameter;;

public class TCCCaller extends Caller<Void> {

    public TCCCaller(TCCManager tccManager) {
        super(tccManager);
    }

    public TCCCaller(TCCManager tccManager, CallerParameter parameter) {
        super(tccManager, parameter);
    }

    public TCCCaller call(ITCCHandler tccHandler) {
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

    public static TCCCaller create(TCCManager tccManager, Object param) {
        TCCCaller tccCaller = TCCCaller.create(tccManager);
        return (TCCCaller) tccCaller.call(p -> {
            ((TCCNodeParameter) p).getTCCState().setParam(param);
            return null;
        });
    }
    
}
