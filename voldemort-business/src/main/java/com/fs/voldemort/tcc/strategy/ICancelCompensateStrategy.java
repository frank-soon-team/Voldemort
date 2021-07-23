package com.fs.voldemort.tcc.strategy;

import com.fs.voldemort.tcc.state.ITCCState;

public interface ICancelCompensateStrategy extends ICompensateStrategy {

    @Override
    default void retry(ITCCState state) {
        // TODO Auto-generated method stub
        
    }
    
}
