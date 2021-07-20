package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCStatus;

public class SimpleTCCBeginBiz extends BaseTCCBiz implements Action1<ITCCState> {

    public SimpleTCCBeginBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear) {

        super(repositoryGear, serializeGear);

    }

    public SimpleTCCBeginBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear,
        IBusinessSupportGear businessSupportGear) {

        super(repositoryGear, serializeGear, businessSupportGear);
    }

    @Override
    public void apply(ITCCState state) {
        if(state.getStatus() != TCCStatus.Initail) {
            throw new IllegalStateException("can not begin, status: " + state.getStatus());
        }

        TCCTaskModel tccModel = changeToTCCModel(state);
        getRepositoryGear().create(tccModel);
    }
    
}
