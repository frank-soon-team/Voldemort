package com.fs.voldemort.tcc.state;

public interface IStateManager {

    void begin(ITCCState state);

    void update(ITCCState state);

    void end(ITCCState state);
    
}
