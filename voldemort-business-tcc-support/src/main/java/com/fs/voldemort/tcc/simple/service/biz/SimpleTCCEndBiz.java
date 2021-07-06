package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.state.ITCCState;

public class SimpleTCCEndBiz implements Action1<ITCCState> {

    @Override
    public void apply(ITCCState state) {
        // TODO 更新TCC任务执行结果
        if(!state.isEnd()) {
            throw new IllegalStateException("can not end, status: " + state.getStatus());
        }
        System.out.println("TCC: " + state.identify() + ", end.");
    }
    
}
