package com.fs.voldemort.tcc.strategy;

public interface ICompensateStrategy {
    
    void beginState();

    void endState();

    

}