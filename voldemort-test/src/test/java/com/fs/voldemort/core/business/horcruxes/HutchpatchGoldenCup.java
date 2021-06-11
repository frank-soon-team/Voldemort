package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BFuncCallable;
import com.fs.voldemort.business.support.BFuncHorcruxes;
import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.core.functional.action.Action2;

@BFuncHorcruxes
public class HutchpatchGoldenCup implements BFuncCallable {

    @BFunc
    public String func(String target,

        @BFuncOperate(BFuncOperate.Oper.SET) Action2<String,Object> f_setC){
        //Add param to call context
        f_setC.apply("c1","context 1 result");
        f_setC.apply("c2","context 2 result");

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
