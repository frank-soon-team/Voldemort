package com.fs.voldemort.business.support;

import java.lang.annotation.*;

/**
 * @author frank
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface FuncContext {

    OPER value();

    enum OPER{
        SET,
        GET,
        DELETE
    }

}
