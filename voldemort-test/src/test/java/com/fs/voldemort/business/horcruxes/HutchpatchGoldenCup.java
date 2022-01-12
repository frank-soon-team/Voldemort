package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.LogicCell;

@LogicCell
public class HutchpatchGoldenCup {

//    @BFunc(fit = FitMode.CUSTOM, iFit = CustomFit.class)
    @BFunc
    public String func(String target){
        return "\n This is Hutchpatch golden cup! Target is " + target;
    }

}