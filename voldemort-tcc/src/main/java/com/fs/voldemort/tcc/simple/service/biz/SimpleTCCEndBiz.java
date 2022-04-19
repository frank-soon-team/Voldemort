package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTask;
import com.fs.voldemort.tcc.state.ITCCState;

public class SimpleTCCEndBiz extends BaseTCCBiz implements Action1<ITCCState> {

    public SimpleTCCEndBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear) {
        super(repositoryGear, serializeGear, null);
    }

    public SimpleTCCEndBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear,
        IBusinessSupportGear businessSupportGear) {
        super(repositoryGear, serializeGear, businessSupportGear);
    }

    @Override
    public void apply(ITCCState state) {
        if(!state.isEnd()) {
            throw new IllegalStateException("can not end, status: " + state.getStatus());
        }
        
        TCCTask tccModel = changeToTCCModel(state);
        getRepositoryGear().update(tccModel);
    }
    
}
