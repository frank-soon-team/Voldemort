package com.fs.voldemort;

import com.fs.voldemort.business.BFuncCaller;
import com.fs.voldemort.business.NodeParam;
import com.fs.voldemort.business.ParallelBFuncCaller;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.parallel.IAsyncStrategy;
import com.fs.voldemort.parallel.ParallelCaller;

public interface Wand {

    public static CallerWand<?> caller() {
        return new CallerWand<>(new Caller());
    }

    public static ParallelWand<?> parallel() {
        return new ParallelWand<>(new ParallelBFuncCaller());
    }

    public static BusinessWand<?> business() {
        return new BusinessWand<>(new BFuncCaller());
    }
    
    static class WandBridge<P extends BaseWand<?>> {

        private final P parentWand;

        public WandBridge(P parentWand) {
            this.parentWand = parentWand;
        }

        public CallerWand<P> caller() {
            return new CallerWand<P>(new Caller(), parentWand);
        }

        public ParallelWand<P> parallel() {
            return new ParallelWand<P>(new ParallelBFuncCaller(), parentWand);
        }

        public ParallelWand<P> parallel(final Func1<Integer, IAsyncStrategy> strategyFactoryFunc) {
            return new ParallelWand<P>(new ParallelBFuncCaller(strategyFactoryFunc), parentWand);
        }

        public BusinessWand<P> business() {
            return new BusinessWand<P>(new BFuncCaller(), parentWand);
        }

    }

    static abstract class BaseWand<P extends BaseWand<?>> {
        private final P parentWand;
        private final Caller caller;

        protected BaseWand(Caller caller, P parentWand) {
            this.caller = caller;
            this.parentWand = parentWand;
        }

        public abstract BaseWand<P> call(Func1<CallerParameter, Object> func);

        public abstract BaseWand<P> call(Caller subCaller);

        public P end() {
            if(parentWand != null) {
                parentWand.call(get());
            }
            return parentWand;
        }

        public Caller get() {
            return caller;
        }

        public Caller into(String name, Object value) {
            return caller.into(name, value);
        }

        protected P parent() {
            return parentWand;
        }
    }

    static class CallerWand<P extends BaseWand<?>> extends BaseWand<P> {

        public CallerWand(Caller caller) {
            this(caller, null);
        }

        protected CallerWand(Caller caller, P parentWand) {
            super(caller, parentWand);
        }

        @Override
        public CallerWand<P> call(Func1<CallerParameter, Object> func) {
            get().call(func);
            return this;
        }

        @Override
        public CallerWand<P> call(Caller subCaller) {
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
        public ParallelWand<P> call(Caller subCaller) {
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

        public BusinessWand(BFuncCaller caller) {
            this(caller, null);
        }

        protected BusinessWand(BFuncCaller caller, P parentWand) {
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
        public BusinessWand<P> call(Caller subCaller) {
            get().call(subCaller);
            return this;
        }

        public BusinessWand<P> call(Class<?> funcClazz) {
            ((BFuncCaller) get()).call(funcClazz);
            return this;
        }

        public BusinessWand<P> call(Class<?> funcClazz, NodeParam...params) {
            ((BFuncCaller) get()).call(funcClazz, params);
            return this;
        }

        public WandBridge<BusinessWand<P>> sub() {
            return new WandBridge<>(this);
        }

    }

}
