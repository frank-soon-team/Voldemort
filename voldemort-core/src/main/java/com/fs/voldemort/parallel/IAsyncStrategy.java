package com.fs.voldemort.parallel;

import java.io.Closeable;

import com.fs.voldemort.core.support.CallerParameter;

public interface IAsyncStrategy extends Closeable {

    void addTaskNode(ParallelTaskNode taskNode, CallerParameter callerParameter, int index);

    ParallelTaskResult execute();
    
}
