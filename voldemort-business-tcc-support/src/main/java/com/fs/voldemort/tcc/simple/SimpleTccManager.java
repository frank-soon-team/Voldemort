package com.fs.voldemort.tcc.simple;

import com.fs.voldemort.tcc.TCCManager;

public class SimpleTCCManager extends TCCManager {

    public SimpleTCCManager() {
        setStateManager(new SimpleStateManager());
        setCancelCompensateStrategy(new SimpleCancelCompensateStrategy());
        setConfirmCompensateStrategy(new SimpleConfirmCompensateStrategy());
    }
    
}
