package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BFuncCallable;
import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;

@BFuncHorcruxes
public class Nagini implements BFuncCallable {

    @BFunc
    public String func(String result, String c1, String c2){
        return "This is Nagini! Target is " + result + "c1:" + c1 + "c2:" + c2;
    }

}
