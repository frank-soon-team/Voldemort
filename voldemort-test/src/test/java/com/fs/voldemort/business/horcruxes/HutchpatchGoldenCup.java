package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.fit.CustomFit;
import com.fs.voldemort.business.fit.FitMode;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.core.functional.action.Action2;

@BFuncHorcruxes
public class HutchpatchGoldenCup {

    @BFunc(fit = FitMode.CUSTOM, iFit = CustomFit.class)
    public String func(String target,
        @BFuncOperate(BFuncOperate.Oper.SET) Action2<String,Object> f_setC){
        //Add param to call context
        f_setC.apply("c1","context 1 result");
        f_setC.apply("c2","context 2 result");

        return "This is Hutchpatch golden cup! Target is " + target;
    }

}