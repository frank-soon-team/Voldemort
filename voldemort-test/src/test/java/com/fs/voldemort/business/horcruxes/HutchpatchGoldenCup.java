package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.fit.CustomFit;
import com.fs.voldemort.business.fit.FitMode;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.business.support.LogicCell;
import com.fs.voldemort.core.functional.action.Action2;

@LogicCell
public class HutchpatchGoldenCup {

//    @BFunc(fit = FitMode.CUSTOM, iFit = CustomFit.class)
    @BFunc
    public String func(String target){
        return "\n This is Hutchpatch golden cup! Target is " + target;
    }

}