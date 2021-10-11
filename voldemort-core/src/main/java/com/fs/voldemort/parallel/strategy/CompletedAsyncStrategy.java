package com.fs.voldemort.parallel.strategy;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.parallel.ParallelTaskNode;
import com.fs.voldemort.parallel.ParallelTaskResult;
import com.fs.voldemort.parallel.ParallelTaskResult.ResultModel;

public class CompletedAsyncStrategy extends BaseAsyncStrategy {

    private CompletableFuture<?>[] taskArray;

    public CompletedAsyncStrategy(int size) {
        this(size, null);
    }    

    public CompletedAsyncStrategy(int size, AbstractExecutorService executorService) {
        super(size, executorService);
        this.taskArray = new CompletableFuture[size];
    }

    @Override
    public void addTaskNode(ParallelTaskNode taskNode, CallerParameter callerParameter, int index) {
        checkState();
        Supplier<Object> task = () -> taskNode.doAction(callerParameter);
        taskArray[index] = 
            this.executor == null 
                ? CompletableFuture.supplyAsync(task) 
                : CompletableFuture.supplyAsync(task, this.executor);
    }

    @Override
    public ParallelTaskResult execute() {
        checkState();
        if(taskArray == null || taskArray.length == 0) {
            return result;
        }

        CompletableFuture<Void> promise = CompletableFuture.allOf(taskArray);
        promise.join();

        for(int i = 0; i < taskArray.length; i++) {
            ResultModel value = new ParallelTaskResult.ResultModel();
            Action1<ResultModel> resultValueSetter = result.getValueSetter(i);
            try {
                value.setValue(taskArray[i].get());
            } catch(Exception e) {
                value.setErrorValue(e);
            }
            resultValueSetter.apply(value);
        }

        return result;
    }
    
}
