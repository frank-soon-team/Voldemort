package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFunc;

@BusinessFuncHorcruxes
public class MarvoroGunterRing implements BusinessFuncCallable {

    @BusinessFunc
    public String func(String result, String c3){
        return "This is Marvoro Gunter Ring! Result is " + result + "c3:" + c3;
    }

}