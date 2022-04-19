package com.fs.voldemort.tcc.simple.adapter;

import com.fs.voldemort.core.functional.action.Action1;

public abstract class BaseSimpleAdapter {

    protected <T> void ensureAction(Action1<T> action, String methodName) {
        if(action == null) {
            throw new NullPointerException("the action of [" + methodName + "] is null.");
        }
    }
    
}
