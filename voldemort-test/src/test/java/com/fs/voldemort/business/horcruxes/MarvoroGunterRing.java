package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.business.support.LogicCell;

@LogicCell
public class MarvoroGunterRing{

    @BFunc
    public String func(String c3, @BFuncOperate(BFuncOperate.Oper.RESULT) String result){
        return "This is Marvoro Gunter Ring! Result is " + result + "c3:" + c3;
    }

}