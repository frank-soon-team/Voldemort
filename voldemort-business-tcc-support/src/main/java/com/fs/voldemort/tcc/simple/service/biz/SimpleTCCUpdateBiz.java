package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCStatus;

public class SimpleTCCUpdateBiz implements Action1<ITCCState> {

    @Override
    public void apply(ITCCState state) {
        // TODO 更新TCC任务 Try阶段执行状态
        TCCStatus status = state.getStatus();
        if(status != TCCStatus.TrySuccess && status != TCCStatus.TryFaild && status != TCCStatus.TryTimeout) {
            throw new IllegalStateException("can not begin, status: " + state.getStatus());
        }
        System.out.println("TCC: " + state.identify() + ", update state.");
    }
    
}
