package com.fs.voldemort.core;

import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.exception.ImperioException;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.func.Act;
import com.fs.voldemort.func.Func;
import lombok.NonNull;

import java.util.function.Consumer;

public class Caller {

    protected final CallerContext context = new CallerContext();
    protected FuncLinkedList funcList = new FuncLinkedList();

    protected Caller() {
    }

    public static Caller create(@NonNull Act<Object> rootAct) {
        Caller caller = create();
        caller.funcList.add(p -> rootAct.act());
        return caller;
    }

    public static Caller create() {
        return new Caller();
    }

    public Caller call(Func<CallerParameter, Object> func) {
        funcList.add(func);
        return this;
    }

    public void exec(@NonNull Consumer<Object> consumer) {
        Object result = exec();
        consumer.accept(result);
    }

    @SuppressWarnings("unchecked")
    public <R> R exec() {
        CallerParameter resultParam = funcList.execute();
        return (R) resultParam.result;
    }

    protected static class FuncLinkedList {

        private CallerNode firstNode;
        private CallerNode lastNode;
        
        public FuncLinkedList() {
        }

        public void add(Func<CallerParameter, Object> func) {
            if(func == null) {
                throw new IllegalArgumentException("the parameter func is required.");
            }
    
            CallerNode node = new CallerNode(func);
    
            if(firstNode == null) {
                firstNode = node;
                return;
            }
    
            if(lastNode == null) {
                lastNode = node;
                firstNode.setNextNode(node);
                return;
            }
    
            lastNode.setNextNode(node);
            lastNode = node;
        }

        public CallerParameter execute() {
            CallerParameter result = CallerParameter.Empty;
            CallerNode currentNode = firstNode;

            int count = 0;
            while(currentNode != null) {
                if(count > 1024) {
                    throw new CrucioException("overflow");
                }
                try {
                    result = createCallParameter(result, currentNode.doAction(result));
                    currentNode = currentNode.getNextNode();
                    count++;
                } catch(Throwable e) {
                    throw new ImperioException("caller excute error.", e);
                }
            }

            return result;
        }

        public CallerNode getFirstNode() {
            return this.firstNode;
        }

        public CallerNode getLastNode() {
            return this.lastNode;
        }

        private CallerParameter createCallParameter(CallerParameter oldParameter, Object result) {
            CallerParameter newParameter = new CallerParameter(result, oldParameter.context());
            return newParameter;
        }

    }

}
