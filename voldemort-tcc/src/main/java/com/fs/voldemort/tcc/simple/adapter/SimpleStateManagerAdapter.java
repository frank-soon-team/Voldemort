package com.fs.voldemort.tcc.simple.adapter;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;

public class SimpleStateManagerAdapter extends BaseSimpleAdapter implements IStateManager {

    private final Action1<ITCCState> beginAction;

    private final Action1<ITCCState> updateAction;

    private final Action1<ITCCState> endAction;

    public SimpleStateManagerAdapter(
        Action1<ITCCState> begin, Action1<ITCCState> update, Action1<ITCCState> end
    ) {
        this.beginAction = begin;
        this.updateAction = update;
        this.endAction = end;
    }

    @Override
    public void begin(ITCCState state) {
        ensureAction(beginAction, "begin");
        beginAction.apply(state);
    }

    @Override
    public void update(ITCCState state) {
        ensureAction(updateAction, "update");
        updateAction.apply(state);
        
    }

    @Override
    public void end(ITCCState state) {
        ensureAction(endAction, "end");
        endAction.apply(state);
    }

    public Action1<ITCCState> getBeginAction() {
        return beginAction;
    }

    public Action1<ITCCState> getUpdateAction() {
        return updateAction;
    }

    public Action1<ITCCState> getEndAction() {
        return endAction;
    }

    
    
}
