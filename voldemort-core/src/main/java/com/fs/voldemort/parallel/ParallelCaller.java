package com.fs.voldemort.parallel;

import java.util.concurrent.ThreadPoolExecutor;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.functional.func.Func2;

public class ParallelCaller extends Caller {

    public ParallelCaller() {
        super(new ParallelTaskList());
    }

    public ParallelCaller(final int capacity, final Func2<Integer, Integer, ThreadPoolExecutor> executorFactoryFunc) {
        super(new ParallelTaskList(capacity, executorFactoryFunc));
    }

    public ParallelCaller(final Func0<ThreadPoolExecutor> executorFactoryFunc) {
        super(new ParallelTaskList((executorSize, maximumSize) -> {
            return executorFactoryFunc.call();
        }));
    }

    public static ParallelCaller create() {
        return new ParallelCaller();
    }

    public static ParallelCaller createWithExecutor(final Func0<ThreadPoolExecutor> executorFactoryFunc) {
        return new ParallelCaller(executorFactoryFunc);
    }

    public static ParallelCaller createWithExecutor(final int capacity, final Func2<Integer, Integer, ThreadPoolExecutor> executorFactoryFunc) {
        return new ParallelCaller(capacity, executorFactoryFunc);
    }
    
}
