package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFuncMark;

@BusinessFuncHorcruxes
public class HutchpatchGoldenCup implements BusinessFuncCallable {

    @BusinessFuncMark
    public String func(String target){
        return "This is Hutchpatch golden cup! Target is " + target;
    }

}
