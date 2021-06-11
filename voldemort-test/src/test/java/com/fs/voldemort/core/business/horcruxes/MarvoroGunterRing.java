package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BFuncCallable;
import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;

@BFuncHorcruxes
public class MarvoroGunterRing implements BFuncCallable {

    @BFunc
    public String func(String result, String c3){
        return "This is Marvoro Gunter Ring! Result is " + result + "c3:" + c3;
    }

}