package com.fs.voldemort.business;

import com.fs.voldemort.business.fit.FitContext;
import com.fs.voldemort.business.fit.FitLibrary;
import com.fs.voldemort.business.paramfinder.ParamFindResult;
import com.fs.voldemort.business.paramfinder.ParamFinderLibrary;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.func.*;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.core.support.FuncLinkedList;
import com.fs.voldemort.core.support.ShareContextCallerParameter;

import java.util.Collection;

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

    private Func1<CallerParameter, Object> generateShellFunc(final Object func, final Func1<Object[],Object> invokeAdaptFunc) {
        Collection<ParamFindResult> fParams = ParamFinderLibrary.f_LambdaParamFinder.getParam(func);
        Func2<Class<?>, String, ?> getIocInstance = (clazz,name)-> BFuncManager.getIocInstanceByName(name);
        return p-> invokeAdaptFunc.call(FitLibrary.f_lambdaFit.call(fParams, new FitContext(p, getIocInstance)).toArray());
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func1 func) {
        super.call(generateShellFunc(func, args -> func.call(args[0])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func2 func) {
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func3 func) {
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func4 func){
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2],args[3])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func5 func){
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2],args[3],args[4])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func6 func){
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2],args[3],args[4],args[5])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func7 func){
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2],args[3],args[4],args[5],args[6])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func8 func){
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7])));
        return this;
    }

    @SuppressWarnings("unchecked")
    public BFuncCaller callFitly(@SuppressWarnings("rawtypes") Func9 func){
        super.call(generateShellFunc(func, args -> func.call(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8])));
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
