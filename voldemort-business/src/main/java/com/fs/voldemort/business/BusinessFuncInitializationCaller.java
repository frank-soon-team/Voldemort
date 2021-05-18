package com.fs.voldemort.business;

import com.fs.voldemort.business.support.BusinessFuncInitializable;
import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.action.Action2;
import com.fs.voldemort.core.functional.func.Func1;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is a business func initializer which used to fill {@link com.fs.voldemort.business.BusinessFunc}
 * for {@link com.fs.voldemort.business.BusinessCaller}
 */
public abstract class BusinessFuncInitializationCaller extends Caller {

    private static final List<Action1<Func1<Class<?>,BusinessFunc>>> initializeGetFuncHookList = new LinkedList<>();

    private static final List<Action1<Action2<Class<?>,BusinessFunc>>> initializeAddFuncHookList = new LinkedList<>();

    public static void initByAnnotation(Func1<Class<? extends Annotation>, Map<String, Object>> getBusinessFuncHorcruxesFunc) {
        final BusinessFuncInitializable businessFuncInitializable = new BusinessFuncContainer();
        final var businessFuncOperational = businessFuncInitializable.init(getBusinessFuncHorcruxesFunc);
        initializeGetFuncHookList.forEach(hook->hook.apply(businessFuncOperational.getFunc()));
        initializeAddFuncHookList.forEach(hook->hook.apply(businessFuncOperational.addFunc()));
    }

    public static void init(Map<String, Object> businessFuncHorcruxesFuncMap) {
        final BusinessFuncInitializable businessFuncInitializable = new BusinessFuncContainer();
        final var businessFuncOperational = businessFuncInitializable.init(businessFuncHorcruxesFuncMap);
        initializeGetFuncHookList.forEach(hook->hook.apply(businessFuncOperational.getFunc()));
        initializeAddFuncHookList.forEach(hook->hook.apply(businessFuncOperational.addFunc()));
    }

    protected static void initializeGetFuncHook(Action1<Func1<Class<?>,BusinessFunc>> hookFunc) {
        initializeGetFuncHookList.add(hookFunc);
    }

    protected static void initializeAddFuncHook(Action1<Action2<Class<?>,BusinessFunc>> hookFunc) {
        initializeAddFuncHookList.add(hookFunc);
    }

}
