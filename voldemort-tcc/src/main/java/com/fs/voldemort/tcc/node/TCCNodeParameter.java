package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.state.ITCCState;

public class TCCNodeParameter extends CallerParameter {

    private final static String TCC_EXECUTE_STATE = "TCC_EXECUTE_STATE";

    public TCCNodeParameter(Object result, CallerContext context) {
        super(result, context);
    }

    public void setTCCState(ITCCState tccState) {
        if(tccState == null) {
            throw new IllegalArgumentException("the parameter [tccState] is required.");
        }
        context().set(TCC_EXECUTE_STATE, tccState);
    }

    public ITCCState getTCCState() {
        return (ITCCState) context().get(TCC_EXECUTE_STATE);
    }

    public Object getParam() {
        ITCCState tccState = getTCCState();
        return tccState != null ? tccState.getParam() : null;
    }
    
}
