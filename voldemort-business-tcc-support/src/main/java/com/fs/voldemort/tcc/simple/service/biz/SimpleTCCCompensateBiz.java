package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;
import com.fs.voldemort.tcc.state.TCCTaskStatus;

/**
 * 简单的TCC补偿实现，将当前的状态切换位WaitingForRetry，并持久化
 */
public class SimpleTCCCompensateBiz extends BaseTCCBiz implements Action1<ITCCState> {

    public SimpleTCCCompensateBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear) {
        super(repositoryGear, serializeGear, null);
    }

    public SimpleTCCCompensateBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear,
        IBusinessSupportGear businessSupportGear) {
        super(repositoryGear, serializeGear, businessSupportGear);
    }

    @Override
    public void apply(ITCCState state) {

        if(state instanceof TCCExecuteState) {
            ((TCCExecuteState) state).setTaskStatus(TCCTaskStatus.WaitingForRetry);
        }

        TCCTaskModel taskModel = changeToTCCModel(state);
        getRepositoryGear().update(taskModel);
    }
    
}
