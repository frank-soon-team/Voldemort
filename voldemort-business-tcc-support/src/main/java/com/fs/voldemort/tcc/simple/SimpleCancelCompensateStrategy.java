package com.fs.voldemort.tcc.simple;

import com.fs.voldemort.tcc.node.BaseTCCHandler;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.strategy.ICancelCompensateStrategy;

public class SimpleCancelCompensateStrategy implements ICancelCompensateStrategy {

    @Override
    public void retry(ITCCState state) {
        System.out.println("TCC: " + state.identify() + ", retry start...");

        state.getTriedNodeList().forEach(n -> {
            BaseTCCHandler tccHandler = (BaseTCCHandler) n.getTCCHandler();
            System.out.println(tccHandler.getName() + " cancel success.");
        });
        
        System.out.println("TCC: " + state.identify() + ", retry end...");
    }
    
}
