package com.fs.voldemort;

import com.fs.voldemort.business.BusinessCaller;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.tcc.TCCCaller;

public final class Wand {

    //#region Caller

    public Caller caller() {
        return Caller.create();
    }

    //#endregion

    //#region BusinessCaller

    public BusinessCaller businessCaller() {
        return BusinessCaller.create();
    }

    //#endregion

    //#region TCCCaller

    public TCCCaller tccCaller() {
        return TCCCaller.create();
    }

    //#endregion
    
}
