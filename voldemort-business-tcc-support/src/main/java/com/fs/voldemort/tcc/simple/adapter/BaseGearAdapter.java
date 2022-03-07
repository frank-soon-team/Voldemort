package com.fs.voldemort.tcc.simple.adapter;

import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;

public abstract class BaseGearAdapter implements ISerializeGear, IRepositoryGear, IBusinessSupportGear {

    @Override
    public String getTraceID(CallerContext context) {
        return null;
    }

    @Override
    public String getBizCode(CallerContext context) {
        return null;
    }

    @Override
    public void setTransactionId(String transactionId) {
        
    }
    
}
