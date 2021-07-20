package com.fs.voldemort.tcc.simple.service.model;

import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.state.ITCCState;

public interface Transfer {

    public static TCCTaskModel toTCCModel(ITCCState tccState, ISerializeGear serializeGear) {
        return null;
    }

    public static ITCCState toTCCState(TCCTaskModel model, ISerializeGear serializeGear) {
        return null;
    }
    
}
