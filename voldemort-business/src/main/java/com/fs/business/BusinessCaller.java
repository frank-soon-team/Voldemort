package com.fs.business;


import com.fs.core.caller.Caller;

/**
 * Polymerize caller
 *
 * @author frank
 */
public class BusinessCaller extends Caller {

    private Caller caller;

    public static BusinessFuncRegistry settleFuncRegistry;

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
