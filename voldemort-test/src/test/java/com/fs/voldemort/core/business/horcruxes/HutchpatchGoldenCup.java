package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BFuncCallable;
import com.fs.voldemort.business.support.BFuncContext;
import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.core.functional.func.Func2;

@BFuncHorcruxes
public class HutchpatchGoldenCup implements BFuncCallable {

    @BFunc
    public String func(String target,
        @BFuncContext(BFuncContext.OPER.SET) Func2<String,Object,Boolean> f_setC){
        //Add param to call context
        f_setC.call("c1","context 1 result");
        this.setC("c2","context 2 result");
        return "This is Hutchpatch golden cup! Target is " + target;
    }

}
//
//public class HutchpatchGoldenCupWrapper extends HutchpatchGoldenCup {
//
//    private final HutchpatchGoldenCup instance;
//
//    private final Context context;
//
//    public HutchpatchGoldenCupWrapper(HutchpatchGoldenCup instance, Context context) {
//
//    }
//
//    public void setC(String k ,Object v) {
//
//    }
//
//}
