package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.business.support.LogicCell;

@LogicCell
public class RoinaRavenclawCrown {
    @BFunc
    public String func(String c1){
        System.out.println("-> RoinaRavenclawCrown c1:" + c1+ "Thread:" + Thread.currentThread().getName());
        return "-> RoinaRavenclawCrown c1:" + c1+ "Thread:" + Thread.currentThread().getName();
    }
}
