package com.fs.voldemort.business;


import com.fs.voldemort.core.Caller;

/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends Caller {

    private Caller caller;

    public static BusinessFuncRegistry businessFuncRegistry;

    private BusinessCaller(Caller caller) {
        this.caller = caller;
    }

    public static BusinessCaller create() {
        return new BusinessCaller(Caller.create());
    }

    public BusinessCaller call(Class funcClazz) {



        return null;


    }






}
