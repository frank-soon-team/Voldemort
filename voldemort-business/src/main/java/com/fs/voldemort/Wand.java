package com.fs.voldemort;

import com.fs.voldemort.business.BusinessCaller;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.parallel.ParallelCaller;
import com.fs.voldemort.tcc.TCCCaller;

public abstract class Wand {

    //#region Caller

    public static Caller caller() {
        return Caller.create();
    }

    public static Caller caller(CallerParameter initParameter) {
        return new Caller(initParameter);
    }

    //#endregion

    //#region BusinessCaller

    public static BusinessCaller businessCaller() {
        return BusinessCaller.create();
    }

    //#endregion

    //#region TCCCaller

    public static TCCCaller tccCaller() {
        return TCCCaller.create();
    }

    //#endregion

    //#region ParallelCaller

    public static ParallelCaller parallelCaller() {
        return ParallelCaller.create();
    }

    //#endregion
    
}
