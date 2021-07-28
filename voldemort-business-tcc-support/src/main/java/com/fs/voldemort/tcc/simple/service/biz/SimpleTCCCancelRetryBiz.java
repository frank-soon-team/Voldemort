package com.fs.voldemort.tcc.simple.service.biz;

import java.util.List;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.state.ITCCState;

public class SimpleTCCCancelRetryBiz extends BaseTCCBiz implements Action1<ITCCState> {

    public SimpleTCCCancelRetryBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear) {

        super(repositoryGear, serializeGear);

    }

    public SimpleTCCCancelRetryBiz(
        IRepositoryGear repositoryGear, 
        ISerializeGear serializeGear,
        IBusinessSupportGear businessSupportGear) {
        super(repositoryGear, serializeGear, businessSupportGear);
    }

    @Override
    public void apply(ITCCState state) {
        if(!state.isSuccess()) {
            return;
        }

        List<TCCNode> triedNodeList = state.getTriedNodeList();
        if(triedNodeList == null || triedNodeList.isEmpty()) {
            throw new IllegalStateException("the TCCNodeList is empty.");
        }

        // TODO 处理回滚补偿流程
    }
    
}
