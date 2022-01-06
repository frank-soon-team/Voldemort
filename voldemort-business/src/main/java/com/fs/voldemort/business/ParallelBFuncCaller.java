package com.fs.voldemort.business;

import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.ShareContextCallerParameter;
import com.fs.voldemort.parallel.IAsyncStrategy;
import com.fs.voldemort.parallel.ParallelCaller;
import com.fs.voldemort.parallel.ParallelTaskList;

public class ParallelBFuncCaller extends ParallelCaller implements ICallWithParameter<ParallelBFuncCaller> {

    public ParallelBFuncCaller() {
        super(new ParallelBFuncLinkedList());
    }

    public ParallelBFuncCaller(Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
        super(new ParallelBFuncLinkedList(strategyFactoryFunc));
    }

    @Override
    public ParallelBFuncCaller call(Class<?> funcClazz, NodeParam... params) {
        ((ParallelBFuncLinkedList) this.funcList).add(p -> BFuncManager.invokeFunc(p, funcClazz), params);
        return this;
    }

    static class ParallelBFuncLinkedList extends ParallelTaskList {

        public ParallelBFuncLinkedList() {
            super();
        }

        public ParallelBFuncLinkedList(Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
            super(strategyFactoryFunc);
        }

        public CallerNode add(Func1<CallerParameter, Object> func, NodeParam... params) {
            CallerNode node = super.add(func);
            ICallWithParameter.setParameterBeforeAction(node, params);
            return node;
        }

        @Override
        protected CallerParameter ensureCallerParameter(CallerParameter parameter) {
            if(parameter == null) {
                return new BFuncParameter(null, new CallerContext());
            }
            return parameter instanceof ShareContextCallerParameter
                        ? parameter
                        : new BFuncParameter(parameter);
        }

        @Override
        protected CallerParameter createCallParameter(CallerParameter oldParameter, Object result) {
            return new BFuncParameter(result, oldParameter.context());
        }

    }
    
}
