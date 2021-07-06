package com.fs.voldemort.tcc.simple.adapter;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.state.ITCCState;

public abstract class BaseSimpleAdapter {

    protected void ensureAction(Action1<ITCCState> action, String methodName) {
        if(action == null) {
            throw new NullPointerException("the action of [" + methodName + "] is null.");
        }
    }
    
}
