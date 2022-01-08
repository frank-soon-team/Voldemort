package com.fs.voldemort.core;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.core.support.ShareContextCallerParameter;

public class Caller<R> {

    protected final FuncLinkedList funcList;
    private final CallerParameter initializationParameter;

    public Caller() {
        this(new FuncLinkedList(), null);
    }

    public Caller(Func0<Object> rootAct) {
        this();
        if(rootAct != null) {
            funcList.add(p -> rootAct.call());
        }
    }

    public Caller(CallerParameter initParameter) {
        this(new FuncLinkedList(), initParameter);
    }

    public Caller(CallerParameter initParameter, boolean shareContext) {
        this(shareContext 
            ? new ShareContextCallerParameter(initParameter.result, initParameter.context()) 
            : initParameter);
    }

    protected Caller(FuncLinkedList funcLinkedList) {
        this(funcLinkedList, null);
    }

    protected Caller(FuncLinkedList funcLinkedList, CallerParameter initCallerParameter) {
        if(funcLinkedList == null) {
            throw new IllegalArgumentException("the parameter funcLinkedList of constructor is required.");
        }
        funcList = funcLinkedList;
        initializationParameter = initCallerParameter;
    }

    public Caller<R> call(Func1<CallerParameter, Object> func) {
        funcList.add(func);
        return this;
    }

    public Caller<R> call(Caller<?> caller) {
        if(caller == null) {
            throw new IllegalArgumentException("the parameter caller is required.");
        }

        funcList.add(p -> caller.exec(p));
        return this;
    }

    public Caller<R> into(String name, Object value) {
        IntoCaller<R> intoCaller = new IntoCaller<R>(this);
        intoCaller.into(name, value);
        
        funcList.add(p -> intoCaller.exec(new ShareContextCallerParameter(p.result, p.context())));
        return intoCaller;
    }

    public void exec(Action1<R> action) {
        if(action == null) {
            throw new IllegalArgumentException("the parameter action is required.");
        }
        R result = exec();
        action.apply(result);
    }

    public R exec() {
        return exec(initializationParameter);
    }
    
    protected R exec(CallerParameter parameter) {
        CallerParameter resultParam = funcList.execute(parameter);
        return resultParam.castResult();
    }

    public static class IntoCaller<R> extends Caller<R> {

        private final Caller<R> parentCaller;

        private IntoCaller(Caller<R> parentCaller) {
            super();
            if(parentCaller == null) {
                throw new IllegalArgumentException("the parameter [initParameter] of constructor is required.");
            }
            this.parentCaller = parentCaller;
        }

        @Override
        public Caller<R> into(String name, Object value) {
            super.call(p -> {
                p.context().set(name, value);
                return p.result;
            });
            return this;
        }

        //#region implements Caller API with parentCaller

        @Override
        public Caller<R> call(Caller<?> caller) {
            return parentCaller.call(caller);
        }

        @Override
        public Caller<R> call(Func1<CallerParameter, Object> func) {
            return parentCaller.call(func);
        }

        @Override
        public R exec() {
            return parentCaller.exec();
        }

        @Override
        public void exec(Action1<R> action) {
            parentCaller.exec(action);
        }

        @Override
        protected R exec(CallerParameter parameter) {
            return super.exec(parameter);
        }

        //#endregion

    }

}
