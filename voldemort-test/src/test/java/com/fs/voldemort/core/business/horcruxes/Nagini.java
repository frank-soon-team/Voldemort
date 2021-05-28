package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFunc;

@BusinessFuncHorcruxes
public class Nagini implements BusinessFuncCallable {

    @BusinessFunc
    public String func(String result, String c1, String c2){
        return "This is Nagini! Target is " + result + "c1:" + c1 + "c2:" + c2;
    }

}
