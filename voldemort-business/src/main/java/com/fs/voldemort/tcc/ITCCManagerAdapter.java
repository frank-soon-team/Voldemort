package com.fs.voldemort.tcc;

import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.strategy.ICompensateStrategy;

public interface ITCCManagerAdapter {

    IStateManager getStateManager();

    ICompensateStrategy getCompensateStrategy();

    Func0<ITCCState> getTCCStateGetter();
    
}
