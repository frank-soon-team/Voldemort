package com.fs.voldemort;

import com.fs.voldemort.business.BFuncCaller;
import java.util.concurrent.ThreadPoolExecutor;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.parallel.ParallelCaller;

public interface Wand {

    public static CallerWand<?> caller() {
        return new CallerWand<>(Caller.create());
    }

    public static ParallelWand<?> parallel() {
        return new ParallelWand<>(ParallelCaller.create());
    }

    public static BusinessWand<?> business() {
        return new BusinessWand<>(BFuncCaller.create());
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
            return new ParallelWand<P>(new ParallelCaller(), parentWand);
        }

        public ParallelWand<P> parallel(final Func0<ThreadPoolExecutor> executorFactoryFunc) {
            return new ParallelWand<P>(new ParallelCaller(executorFactoryFunc), parentWand);
        }

        public ParallelWand<P> parallel(final int capacity, final Func2<Integer, Integer, ThreadPoolExecutor> executorFactoryFunc) {
            return new ParallelWand<P>(new ParallelCaller(capacity, executorFactoryFunc), parentWand);
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

        @Override
        public BusinessWand<P> call(Caller subCaller) {
            get().call(subCaller);
            return this;
        }

        public BusinessWand<P> call(Class<?> funcClazz) {
            ((BFuncCaller) get()).call(funcClazz);
            return this;
        }

        public WandBridge<BusinessWand<P>> sub() {
            return new WandBridge<>(this);
        }

    }

}
