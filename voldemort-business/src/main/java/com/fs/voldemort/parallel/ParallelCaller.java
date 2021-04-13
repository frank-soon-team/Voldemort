package com.fs.voldemort.parallel;

import com.fs.voldemort.core.Caller;

public class ParallelCaller extends Caller {

    public ParallelCaller() {
        super(new ParallelTaskList());
    }

    public static ParallelCaller create() {
        return new ParallelCaller();
    }
    
}
