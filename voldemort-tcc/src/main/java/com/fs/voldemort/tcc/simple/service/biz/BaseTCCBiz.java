package com.fs.voldemort.tcc.simple.service.biz;

import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;
import com.fs.voldemort.tcc.simple.service.model.Transfer;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;

public abstract class BaseTCCBiz {

    private IBusinessSupportGear businessSupportGear;

    private IRepositoryGear repositoryGear;

    private ISerializeGear serializeGear;

    public BaseTCCBiz(
        IRepositoryGear repositoryGear, ISerializeGear serializeGear) {

        this(repositoryGear, serializeGear, null);
    }

    public BaseTCCBiz(
        IRepositoryGear repositoryGear,
        ISerializeGear serializeGear,
        IBusinessSupportGear businessSupportGear) {

        this.repositoryGear = repositoryGear;
        this.serializeGear = serializeGear;
        this.businessSupportGear = businessSupportGear;
    }

    protected IRepositoryGear getRepositoryGear() {
        return this.repositoryGear;
    }

    protected ISerializeGear getSerializeGear() {
        return this.serializeGear;
    }

    protected IBusinessSupportGear getBusinessSupportGear() {
        return this.businessSupportGear;
    }

    protected TCCTaskModel changeToTCCModel(ITCCState tccState) {
        TCCTaskModel tccModel = Transfer.toTCCModel(tccState, serializeGear);
        if(businessSupportGear != null) {
            CallerContext context = getContext(tccState);
            if(context != null) {
                tccModel.setTraceId(businessSupportGear.getTraceID(context));
                tccModel.setBizCode(businessSupportGear.getBizCode(context));
            }
        }
        return tccModel;
    }

    protected ITCCState changeToTCCState(TCCTaskModel tccModel) {
        return Transfer.toTCCState(tccModel, serializeGear);
    }

    protected CallerContext getContext(ITCCState tccState) {
        CallerContext context = null;
        if(tccState instanceof TCCExecuteState) {
            context = ((TCCExecuteState) tccState).getContext();
        }
        return context;
    } 
    
}
