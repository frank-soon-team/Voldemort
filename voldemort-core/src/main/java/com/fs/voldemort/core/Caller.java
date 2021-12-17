package com.fs.voldemort.core;

import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.core.support.ShareContextCallerParameter;

public class Caller {

    protected final FuncLinkedList funcList;
    private final CallerParameter initializationParameter;

    public Caller() {
        this(new FuncLinkedList(), null);
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

    public Caller call(Func1<CallerParameter, Object> func) {
        funcList.add(func);
        return this;
    }

    public Caller call(Caller caller) {
        if(caller == null) {
            throw new IllegalArgumentException("the parameter caller is required.");
        }

        funcList.add(p -> caller.exec(p));
        return this;
    }

    public Caller into(String name, Object value) {
        IntoCaller intoCaller = new IntoCaller(this);
        intoCaller.into(name, value);
        
        funcList.add(p -> intoCaller.exec(new ShareContextCallerParameter(p.result, p.context())));
        return intoCaller;
    }

    public void exec(Action1<Object> action) {
        if(action == null) {
            throw new IllegalArgumentException("the parameter action is required.");
        }
        Object result = exec();
        action.apply(result);
    }

    @SuppressWarnings("unchecked")
    public <R> R exec() {
        return (R) exec(initializationParameter);
    }
    
    protected Object exec(CallerParameter parameter) {
        CallerParameter resultParam = funcList.execute(parameter);
        return resultParam.result;
    }

    //#region Factory Functions

    public static Caller create() {
        return new Caller();
    }
    
    public static Caller create(Func0<Object> rootAct) {
        Caller caller = create();
        if(rootAct != null) {
            caller.funcList.add(p -> rootAct.call());
        }
        return caller;
    }

    public static Caller create(CallerParameter initParameter) {
        return new Caller(initParameter);
    }

    public static Caller createWithContext(CallerParameter initParameter) {
        return new Caller(initParameter, true);
    }

    //#endregion

    public static class IntoCaller extends Caller {

        private final Caller parentCaller;

        private IntoCaller(Caller parentCaller) {
            super();
            if(parentCaller == null) {
                throw new IllegalArgumentException("the parameter [initParameter] of constructor is required.");
            }
            this.parentCaller = parentCaller;
        }

        @Override
        public Caller into(String name, Object value) {
            super.call(p -> {
                p.context().set(name, value);
                return p.result;
            });
            return this;
        }

        //#region implements Caller API with parentCaller

        @Override
        public Caller call(Caller caller) {
            return parentCaller.call(caller);
        }

        @Override
        public Caller call(Func1<CallerParameter, Object> func) {
            return parentCaller.call(func);
        }

        @Override
        public <R> R exec() {
            return parentCaller.exec();
        }

        @Override
        public void exec(Action1<Object> action) {
            parentCaller.exec(action);
        }

        @Override
        protected Object exec(CallerParameter parameter) {
            return super.exec(parameter);
        }

        //#endregion

    }

}
