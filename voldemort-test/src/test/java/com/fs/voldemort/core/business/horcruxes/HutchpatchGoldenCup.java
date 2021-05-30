package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.FuncContext;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFunc;
import com.fs.voldemort.core.functional.func.Func2;

@BusinessFuncHorcruxes
public class HutchpatchGoldenCup implements BusinessFuncCallable {

    @BusinessFunc
    public String func(String target,
        
        @FuncContext(FuncContext.OPER.SET) Func2<String,Object,Boolean> f_setC){
        //Add param to call context
        f_setC.call("c1","context 1 result");
        return "This is Hutchpatch golden cup! Target is " + target;
    }

}
