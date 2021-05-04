package com.fs.voldemort.tcc.simple;

import com.fs.voldemort.tcc.TCCManager;

public class SimpleTccManager extends TCCManager {

    public SimpleTccManager() {
        setStateManager(new SimpleStateManager());
        setCancelCompensateStrategy(new SimpleCancelCompensateStrategy());
        setConfirmCompensateStrategy(new SimpleConfirmCompensateStrategy());
    }
    
}
