package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;

@BFuncHorcruxes
public class MarvoroGunterRing{

    @BFunc
    public String func(String result, String c3){
        return "This is Marvoro Gunter Ring! Result is " + result + "c3:" + c3;
    }

}