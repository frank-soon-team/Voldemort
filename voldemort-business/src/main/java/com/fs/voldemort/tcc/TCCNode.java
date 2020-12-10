package com.fs.voldemort.tcc;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.func.Func;

public class TCCNode extends CallerNode {

    private Func<CallerParameter, Object> cancelFunc;

    public TCCNode(Func<CallerParameter, Object> actionFunc, Func<CallerParameter, Object> cancelFunc) {
        super(actionFunc);
        this.cancelFunc = cancelFunc;
    }

    public void doCancel(CallerParameter cancelParameter) {

    }
    
}
