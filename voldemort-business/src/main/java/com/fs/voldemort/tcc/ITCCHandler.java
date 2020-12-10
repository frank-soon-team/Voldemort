package com.fs.voldemort.tcc;

public interface ITCCHandler {
    
    boolean goTry();

    void confirm();

    void cancel();

}
