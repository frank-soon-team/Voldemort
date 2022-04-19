package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTask;
import com.fs.voldemort.tcc.simple.service.model.Transfer;
import com.fs.voldemort.tcc.state.ITCCState;

public class SimpleTCCStateBiz extends BaseTCCBiz implements Func1<String, ITCCState> {

    public SimpleTCCStateBiz(IRepositoryGear repositoryGear, ISerializeGear serializeGear) {
        super(repositoryGear, serializeGear);
    }

    @Override
    public ITCCState call(String tccTransactionId) {
        TCCTask taskModel = getRepositoryGear().get(tccTransactionId);
        ITCCState tccState = Transfer.toTCCState(taskModel, getSerializeGear());
        return tccState;
    }
    
}
