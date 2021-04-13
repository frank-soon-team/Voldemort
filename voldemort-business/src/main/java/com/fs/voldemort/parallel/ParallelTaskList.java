package com.fs.voldemort.parallel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.func.Func;
import com.fs.voldemort.func.Func2;

public class ParallelTaskList extends FuncLinkedList {

    private final static String THREAD_NAME = "ParallelCaller";

    private final int capacity;
    private final Func2<Integer, Integer, ThreadPoolExecutor> executorFactoryFunc;


    public ParallelTaskList() {
        this(-1, null);
    }

    public ParallelTaskList(final int capacity) {
        this(capacity, null);
    }

    public ParallelTaskList(final int capacity, Func2<Integer, Integer, ThreadPoolExecutor> executorFactoryFunc) {
        this.capacity = capacity;
        this.executorFactoryFunc = executorFactoryFunc != null ? executorFactoryFunc : createExecutorFactoryFunc();
    }

    @Override
    public void add(Func<CallerParameter, Object> func) {
        if(func == null) {
            throw new IllegalArgumentException("the parameter func is required.");
        }

        ParallelTaskNode node = new ParallelTaskNode(func);
        add(node);
    }

    @Override
    public CallerParameter execute(CallerParameter parameter) {
        int executeCapacity = capacity;
        if(executeCapacity == -1) {
            executeCapacity = Runtime.getRuntime().availableProcessors();
        }

        int size = this.size();
        if(executeCapacity > size) {
            executeCapacity = size;
        }

        ThreadPoolExecutor executor = executorFactoryFunc.call(executeCapacity, size);
        ParallelTaskResult result = new ParallelTaskResult(size);
        CallerParameter currentParameter = ensureCallerParameter(parameter);
        try {
            CountDownLatch latch = new CountDownLatch(size);
            ParallelTaskNode currentNode = (ParallelTaskNode) getFirstNode();
            
            int index = 0;
            while(currentNode != null) {
                currentNode.setCurrentCountDownLatch(latch);
                currentNode.setCurrentCallerParameter(currentParameter);
                currentNode.setCurrentResultSetter(result.getValueSetter(index));
                executor.execute(currentNode);

                currentNode = (ParallelTaskNode) currentNode.getNextNode();
                index++;
            }
            latch.await();

        } catch(Exception e) {
            throw new CrucioException("execute the parallel caller error.", e);
        } finally {
            executor.shutdown();
        }
        return createCallParameter(currentParameter, result);
    }

    private static Func2<Integer, Integer, ThreadPoolExecutor> createExecutorFactoryFunc() {
        return (executorSize, maximumSize) -> {
            return new ThreadPoolExecutor(
                executorSize, maximumSize, 0L, TimeUnit.MILLISECONDS, 
                new LinkedBlockingQueue<Runnable>(maximumSize.intValue()),
                new ParallelExecutorThreadFactory(THREAD_NAME),
                new CallerRunsPolicy());
        };
    }
    
}
