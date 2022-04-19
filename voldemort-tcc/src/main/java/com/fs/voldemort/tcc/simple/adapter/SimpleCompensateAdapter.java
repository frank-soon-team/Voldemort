package com.fs.voldemort.tcc.simple.adapter;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.strategy.ICompensateStrategy;

public class SimpleCompensateAdapter extends BaseSimpleAdapter implements ICompensateStrategy {

    private final Action1<ITCCState> retryAction;

    public SimpleCompensateAdapter(
        Action1<ITCCState> retry
    ) {
        this.retryAction = retry;
    }

    @Override
    public void retry(ITCCState state) {
        ensureAction(retryAction, "retry");
        retryAction.apply(state);
    }
    
}
