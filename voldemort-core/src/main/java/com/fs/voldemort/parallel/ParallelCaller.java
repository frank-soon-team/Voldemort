package com.fs.voldemort.parallel;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.Func1;

public class ParallelCaller extends Caller {

    //#region constructors

    public ParallelCaller() {
        super(new ParallelTaskList());
    }

    public ParallelCaller(final Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
        super(new ParallelTaskList(strategyFactoryFunc));
    }

    protected ParallelCaller(ParallelTaskList taskList) {
        super(taskList);
    }

    //#endregion


    //#region factory methods

    public static ParallelCaller create() {
        return new ParallelCaller();
    }

    public static ParallelCaller createWithExecutor(final Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
        return new ParallelCaller(strategyFactoryFunc);
    }

    //#endregion
    
}
