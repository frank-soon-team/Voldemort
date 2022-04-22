package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.constant.ContextKeys;
import com.fs.voldemort.tcc.state.ITCCState;

public class TCCNodeParameter extends CallerParameter {

    public TCCNodeParameter(Object result, CallerContext context) {
        super(result, context);
    }

    public void setTCCState(ITCCState tccState) {
        if(tccState == null) {
            throw new IllegalArgumentException("the parameter [tccState] is required.");
        }
        context().set(ContextKeys.TccExecuteState, tccState);
    }

    public ITCCState getTCCState() {
        return (ITCCState) context().get(ContextKeys.TccExecuteState);
    }

    public Object getParam() {
        ITCCState tccState = getTCCState();
        return tccState != null ? tccState.getParam() : null;
    }

    public void setResult(Object resultValue) {
        this.context().set(ContextKeys.TccCurrentNodeResult, resultValue);
    }

}
