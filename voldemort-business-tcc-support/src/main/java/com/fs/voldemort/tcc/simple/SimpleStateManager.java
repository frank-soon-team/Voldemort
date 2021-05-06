package com.fs.voldemort.tcc.simple;

import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCStatus;

public class SimpleStateManager implements IStateManager {

    @Override
    public void begin(ITCCState state) {
        if(state.getStatus() != TCCStatus.Initail) {
            throw new IllegalStateException("can not begin, status: " + state.getStatus());
        }
        System.out.println("TCC: " + state.identify() + ", begin.");
    }

    @Override
    public void update(ITCCState state) {
        TCCStatus status = state.getStatus();
        if(status != TCCStatus.TrySuccess && status != TCCStatus.TryFaild && status != TCCStatus.TryTimeout) {
            throw new IllegalStateException("can not begin, status: " + state.getStatus());
        }
        System.out.println("TCC: " + state.identify() + ", update state.");
    }

    @Override
    public void end(ITCCState state) {
        if(!state.isEnd()) {
            throw new IllegalStateException("can not end, status: " + state.getStatus());
        }
        System.out.println("TCC: " + state.identify() + ", end.");
    }
    
}
