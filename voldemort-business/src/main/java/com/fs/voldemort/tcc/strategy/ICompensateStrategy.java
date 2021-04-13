package com.fs.voldemort.tcc.strategy;

import com.fs.voldemort.tcc.state.ITCCState;

public interface ICompensateStrategy {
    
    void retry(ITCCState state);

}