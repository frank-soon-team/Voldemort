package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCStatus;

public class SimpleTCCBeginBiz implements Action1<ITCCState> {

    @Override
    public void apply(ITCCState state) {
        // TODO 创建TCC 任务记录

        if(state.getStatus() != TCCStatus.Initail) {
            throw new IllegalStateException("can not begin, status: " + state.getStatus());
        }

        System.out.println("TCC: " + state.identify() + ", begin.");
    }
    
}
