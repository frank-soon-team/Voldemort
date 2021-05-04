package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncInitializable;
import com.fs.voldemort.business.support.BusinessFuncOperational;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.action.Action2;
import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BusinessFuncInitializationCaller extends Caller {

    private static List<Action1<Func1<Class<?>,BusinessFunc>>> initializeGetFuncHookList = new LinkedList<>();

    private static List<Action2<Class<?>,BusinessFunc>> initializeAddFuncHookList = new LinkedList<>();

    public static void init(Func1<Class<? extends Annotation>, Map<String, Object>> getBusinessFuncHorcruxesFunc) {
        final BusinessFuncInitializable businessFuncInitializable = new BusinessFuncContainer();
        final BusinessFuncOperational businessFuncOperational = businessFuncInitializable.init(getBusinessFuncHorcruxesFunc);
        initializeGetFuncHookList.stream().forEach(hook->hook.apply(businessFuncOperational.getFunc()));
        //TODO:需要解决函数列表不匹配的问题
//        initializeAddFuncHookList.stream().forEach(hook->hook.apply(businessFuncOperational.addFunc()));
    }

    protected static void initializeGetFuncHook(Action1<Func1<Class<?>,BusinessFunc>> hookFunc) {
        initializeGetFuncHookList.add(hookFunc);
    }

    protected static void initializeAddFuncHook(Action2<Class<?>,BusinessFunc> hookFunc) {
        initializeAddFuncHookList.add(hookFunc);
    }

}