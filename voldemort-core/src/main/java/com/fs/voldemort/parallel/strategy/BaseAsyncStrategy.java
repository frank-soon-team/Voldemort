package com.fs.voldemort.parallel.strategy;

import java.io.IOException;
import java.util.concurrent.AbstractExecutorService;

import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.parallel.IAsyncStrategy;
import com.fs.voldemort.parallel.ParallelTaskResult;

public abstract class BaseAsyncStrategy implements IAsyncStrategy {

    protected final ParallelTaskResult result;
    protected final AbstractExecutorService executor;
    private boolean closed = false;

    public BaseAsyncStrategy(int size, AbstractExecutorService executorService) {
        this.result = new ParallelTaskResult(size);
        this.executor = executorService;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    protected void checkState() {
        if(closed) {
            throw new ImperioException("the instance of IAsyncStrategy was closed.");
        }
    }
    
}
