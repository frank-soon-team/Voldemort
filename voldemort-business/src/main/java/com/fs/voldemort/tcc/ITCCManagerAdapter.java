package com.fs.voldemort.tcc;

import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.strategy.ICancelCompensateStrategy;
import com.fs.voldemort.tcc.strategy.IConfirmCompensateStrategy;

public interface ITCCManagerAdapter {

    IStateManager getStateManager();

    IConfirmCompensateStrategy getConfirmCompensateStrategy();

    ICancelCompensateStrategy getCancelCompensateStrategy();
    
}
