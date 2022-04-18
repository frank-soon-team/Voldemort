package com.fs.voldemort.parallel;

import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.parallel.strategy.CompletedAsyncStrategy;

public class ParallelTaskList extends FuncLinkedList {
    
    private final Func1<Integer, IAsyncStrategy> strategyFactory;


    public ParallelTaskList() {
        this(size -> new CompletedAsyncStrategy(size));
    }

    public ParallelTaskList(Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
        this.strategyFactory = strategyFactoryFunc;
    }

    @Override
    public CallerNode add(Func1<CallerParameter, Object> func) {
        if(func == null) {
            throw new IllegalArgumentException("the parameter [func] is required.");
        }

        ParallelTaskNode node = new ParallelTaskNode(func);
        add(node);
        return node;
    }

    @Override
    public CallerParameter execute(CallerParameter parameter) {
        int size = this.size();

        ParallelTaskResult result = null;
        ParallelTaskNode currentNode = (ParallelTaskNode) getFirstNode();

        CallerParameter currentParameter = ensureCallerParameter(parameter);

        try(IAsyncStrategy asyncStrategy = strategyFactory.call(size)) {
            int index = 0;
            while(currentNode != null) {
                asyncStrategy.addTaskNode(currentNode, currentParameter, index);
                currentNode = (ParallelTaskNode) currentNode.getNextNode();
                index++;
            }
            result = asyncStrategy.execute();
        } catch(ImperioException e) {
            throw e;
        } catch(Exception e) {
            throw new ImperioException("execute the parallel caller error.", e);
        }

        return createCallParameter(currentParameter, result);
    }
    
}
