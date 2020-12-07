package com.fs.voldemort;

import com.fs.voldemort.business.BusinessCaller;
import com.fs.voldemort.core.Caller;

public final class Voldemort {

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

    //#endregion
    
}
