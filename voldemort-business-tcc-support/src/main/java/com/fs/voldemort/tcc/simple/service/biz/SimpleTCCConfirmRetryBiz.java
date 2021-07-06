package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.node.BaseTCCHandler;
import com.fs.voldemort.tcc.state.ITCCState;

public class SimpleTCCConfirmRetryBiz implements Action1<ITCCState> {

    @Override
    public void apply(ITCCState state) {
        // TODO TCC 确认阶段重试

        System.out.println("TCC: " + state.identify() + ", retry start...");

        state.getTriedNodeList().forEach(n -> {
            BaseTCCHandler tccHandler = (BaseTCCHandler) n.getTCCHandler();
            System.out.println(tccHandler.getName() + " confirm success.");
        });
        
        System.out.println("TCC: " + state.identify() + ", retry end...");
        
    }
    
}
