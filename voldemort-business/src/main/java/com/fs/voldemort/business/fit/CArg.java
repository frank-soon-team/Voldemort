package com.fs.voldemort.business.fit;

import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.core.support.CallerContext;

public class CArg {

    public final BFuncOperate.Oper oper;
    public final CallerContext context;
    public final Object result;

    public CArg(BFuncOperate.Oper oper, CallerContext context, Object result) {
        this.oper = oper;
        this.context = context;
        this.result = result;
    }

    public Object getOperFunc() {
        return oper.getFunc.call(context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var args = (CArg) o;
        return oper.equals(args.oper);
    }
}
