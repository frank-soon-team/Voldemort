package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.support.*;
import com.fs.voldemort.core.functional.action.Action2;

@LogicCell
public class Nagini{

    @BFunc
    public String func(String result,
                       @BFuncContext String c1, @BFuncContext String c2,
                       @BFuncContext String cNull,
                       @BFuncOperate(BFuncOperate.Oper.SET) Action2<String,Object> f_setC){
        return "This is Nagini! Target is " + result + "c1:" + c1 + "c2:" + c2;
    }

}
