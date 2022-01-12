package com.fs.voldemort;

import com.fs.voldemort.business.BFuncCaller;
import com.fs.voldemort.business.NodeParam;
import com.fs.voldemort.business.ParallelBFuncCaller;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.*;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.parallel.IAsyncStrategy;
import com.fs.voldemort.parallel.ParallelCaller;

public interface Wand {

    public static CallerWand<?> caller() {
        return new CallerWand<>(new Caller<Object>());
    }

    public static ParallelWand<?> parallel() {
        return new ParallelWand<>(new ParallelBFuncCaller());
    }

    public static BusinessWand<?> business() {
        return new BusinessWand<>(new BFuncCaller<Object>());
    }
    
    static class WandBridge<P extends BaseWand<?>> {

        private final P parentWand;

        public WandBridge(P parentWand) {
            this.parentWand = parentWand;
        }

        public CallerWand<P> caller() {
            return new CallerWand<P>(new Caller<Object>(), parentWand);
        }

        public ParallelWand<P> parallel() {
            return new ParallelWand<P>(new ParallelBFuncCaller(), parentWand);
        }

        public ParallelWand<P> parallel(final Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
            return new ParallelWand<P>(new ParallelBFuncCaller(strategyFactoryFunc), parentWand);
        }

        public BusinessWand<P> business() {
            return new BusinessWand<P>(new BFuncCaller<Object>(), parentWand);
        }

    }

    static abstract class BaseWand<P extends BaseWand<?>> {
        private final P parentWand;
        private final Caller<?> caller;

        protected BaseWand(Caller<?> caller, P parentWand) {
            this.caller = caller;
            this.parentWand = parentWand;
        }

        public abstract BaseWand<P> call(Func1<CallerParameter, Object> func);

        public abstract BaseWand<P> call(Caller<?> subCaller);

        public P end() {
            if(parentWand != null) {
                parentWand.call(get());
            }
            return parentWand;
        }

        public BaseWand<P> into(String name, Object value) {
            caller.into(name, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <R> R exec() {
            return (R) get().exec();
        }

        public void exec(Action1<Object> action) {
            if(action == null) {
                throw new IllegalArgumentException("the parameter action is required.");
            }
            Object result = get().exec();
            action.apply(result);
        }

        protected P parent() {
            return parentWand;
        }

        protected Caller<?> get() {
            return caller;
        }
    }

    static class CallerWand<P extends BaseWand<?>> extends BaseWand<P> {

        public CallerWand(Caller<?> caller) {
            this(caller, null);
        }

        protected CallerWand(Caller<?> caller, P parentWand) {
            super(caller, parentWand);
        }

        @Override
        public CallerWand<P> call(Func1<CallerParameter, Object> func) {
            get().call(func);
            return this;
        }

        @Override
        public CallerWand<P> call(Caller<?> subCaller) {
            get().call(subCaller);
            return this;
        }

        public WandBridge<? extends CallerWand<P>> sub() {
            return new WandBridge<>(this);
        }

    }

    static class ParallelWand<P extends BaseWand<?>> extends BaseWand<P> {

        public ParallelWand(ParallelCaller parallelCaller) {
            this(parallelCaller, null);
        }

        protected ParallelWand(ParallelCaller parallelCaller, P parentWand) {
            super(parallelCaller, parentWand);
        }

        @Override
        public ParallelWand<P> call(Func1<CallerParameter, Object> func) {
            get().call(func);
            return this;
        }

        @Override
        public ParallelWand<P> call(Caller<?> subCaller) {
            get().call(subCaller);
            return this;
        }

        public ParallelWand<P> call(Class<?> funcClazz) {
            ((ParallelBFuncCaller) get()).call(funcClazz);
            return this;
        }

        public ParallelWand<P> call(Class<?> funcClazz, NodeParam...params) {
            ((ParallelBFuncCaller) get()).call(funcClazz, params);
            return this;
        }

        public WandBridge<ParallelWand<P>> sub() {
            return new WandBridge<>(this);
        }
        
    }

    static class BusinessWand<P extends BaseWand<?>> extends BaseWand<P> {

        public BusinessWand(BFuncCaller<Object> caller) {
            this(caller, null);
        }

        protected BusinessWand(BFuncCaller<Object> caller, P parentWand) {
            super(caller, parentWand);
        }

        @Override
        public BusinessWand<P> call(Func1<CallerParameter, Object> func) {
            get().call(func);
            return this;
        }

        public BusinessWand<P> call(Action1<CallerParameter> action) {
            get().call(p -> {
                action.apply(p);
                return null;
            });
            return this;
        }

        @Override
        public BusinessWand<P> call(Caller<?> subCaller) {
            get().call(subCaller);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> call(Class<?> funcClazz) {
            ((BFuncCaller<Object>) get()).call(funcClazz);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> call(Class<?> funcClazz, NodeParam...params) {
            ((BFuncCaller<Object>) get()).call(funcClazz, params);
            return this;
        }

        public WandBridge<BusinessWand<P>> sub() {
            return new WandBridge<>(this);
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func1<?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func2<?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func3<?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func4<?, ?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func5<?, ?, ?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func6<?, ?, ?, ?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func7<?, ?, ?, ?, ?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func8<?, ?, ?, ?, ?, ?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }

        @SuppressWarnings("unchecked")
        public BusinessWand<P> callFitly(Func9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> func) {
            ((BFuncCaller<Object>) get()).callFitly(func);
            return this;
        }
    }
}
