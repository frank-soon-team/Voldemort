package com.fs.voldemort.tcc;

public class SimpleTccManager extends TCCManager {

    public SimpleTccManager() {
        setStateManager(null);
        setCancelCompensateStrategy(null);
        setConfirmCompensateStrategy(null);
    }
    
}
