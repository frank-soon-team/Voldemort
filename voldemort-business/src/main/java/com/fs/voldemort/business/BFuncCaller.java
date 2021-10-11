package com.fs.voldemort.business;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.core.support.ShareContextCallerParameter;

/**
 * Polymerize caller
 *
 * @author frank
 */
public class BFuncCaller extends Caller implements ICallWithParameter<BFuncCaller> {

    public BFuncCaller() {
        super(new BFuncLinkedList());
    }

    @Override
    public BFuncCaller call(Class<?> funcClazz, NodeParam... params) {
        ((BFuncLinkedList) this.funcList).add(p -> BFuncManager.invokeFunc(p, funcClazz), params);
        return this;
    }

    @Override
    public BFuncCaller call(Func1<CallerParameter, Object> func) {
        super.call(func);
        return this;
    }

    public static BFuncCaller create() {
        return new BFuncCaller();
    }

    static class BFuncLinkedList extends FuncLinkedList {

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
