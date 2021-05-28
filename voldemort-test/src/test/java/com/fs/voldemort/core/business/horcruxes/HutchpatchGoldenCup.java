package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFunc;

@BusinessFuncHorcruxes
public class HutchpatchGoldenCup implements BusinessFuncCallable {

    @BusinessFunc
    public String func(String target){
        //Add param to call context
        return "This is Hutchpatch golden cup! Target is " + target;
    }

}
