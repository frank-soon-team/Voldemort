package com.fs.voldemort.tcc.state;

public interface IStateManager {

    /** INSERT record */
    void begin(ITCCState state);

    /** Just UPDATE task status */
    void update(ITCCState state);

    /** Just UPDATE task status */
    void end(ITCCState state);
    
}
