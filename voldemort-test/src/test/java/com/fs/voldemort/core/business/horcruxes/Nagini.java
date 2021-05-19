package com.fs.voldemort.core.business.horcruxes;

import com.fs.voldemort.business.support.BusinessFuncCallable;
import com.fs.voldemort.business.support.BusinessFuncHorcruxes;
import com.fs.voldemort.business.support.BusinessFuncMark;

@BusinessFuncHorcruxes
public class Nagini implements BusinessFuncCallable {

    @BusinessFuncMark
    public String func(String target){
        return "This is Nagini! Target is " + target;
    }

}
