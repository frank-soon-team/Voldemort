package com.fs.voldemort.parallel.strategy;

import java.io.IOException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.parallel.ParallelExecutorThreadFactory;
import com.fs.voldemort.parallel.ParallelTaskNode;
import com.fs.voldemort.parallel.ParallelTaskResult;
import com.fs.voldemort.parallel.ParallelTaskResult.ResultModel;

public class LatchAsyncStrategy extends BaseAsyncStrategy {

    private final static String THREAD_NAME = "ParallelCaller";
    private CountDownLatch latch;
    private boolean shutdownExectorFlag = true;

    public LatchAsyncStrategy(int size) {
        this(size, createExecutor(-1, size), true);
    }

    public LatchAsyncStrategy(int size, AbstractExecutorService executorService) {
        this(size, executorService, false);
    }

    public LatchAsyncStrategy(int size, AbstractExecutorService executorService, boolean shutdownExectorFlag) {
        super(size, executorService);
        this.latch = new CountDownLatch(size);
        this.shutdownExectorFlag = shutdownExectorFlag;
    }

    @Override
    public void addTaskNode(ParallelTaskNode taskNode, CallerParameter callerParameter, int index) {
        checkState();

        final Action1<ResultModel> resultValueSetter = result.getValueSetter(index);
        executor.execute(() -> {
            ResultModel result = new ResultModel();
            try {
                Object value = taskNode.doAction(callerParameter);
                result.setValue(value);
                if(latch != null) {
                    latch.countDown();
                }
            } catch(Exception e) {
                result.setValue(new ImperioException("execute parallelTask error.", e));
            } finally {
                resultValueSetter.apply(result);
            }
        });
        
    }

    @Override
    public ParallelTaskResult execute() {
        checkState();

        try {
            latch.await();
            return result;
        } catch(InterruptedException e) {
            throw new ImperioException("failed waitting for ParallelTaskResult.", e);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        if(this.executor != null && shutdownExectorFlag) {
            this.executor.shutdown();
        }
        this.latch = null;
    }

    private static AbstractExecutorService createExecutor(int capacity, int size) {
        int executeCapacity = capacity;
        if(executeCapacity == -1) {
            executeCapacity = Runtime.getRuntime().availableProcessors();
        }

        if(executeCapacity > size) {
            executeCapacity = size;
        }

        return new ThreadPoolExecutor(
            executeCapacity, size, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(size),
            new ParallelExecutorThreadFactory(THREAD_NAME),
            new ThreadPoolExecutor.CallerRunsPolicy());
    }
    
}
