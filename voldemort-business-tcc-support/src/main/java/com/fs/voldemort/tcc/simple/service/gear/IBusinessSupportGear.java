package com.fs.voldemort.tcc.simple.service.gear;

import com.fs.voldemort.core.support.CallerContext;

public interface IBusinessSupportGear {

    String getTraceID(CallerContext context);

    String getBizCode(CallerContext context);

    void setTransactionId(String transactionId);
    
}
