package com.fs.voldemort.business.support;

import com.fs.voldemort.core.functional.action.Action2;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerContext;

import java.lang.annotation.*;

/**
 * @author frank
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BFuncOperate {

    Oper value();

    /**
     * @author frank
     */
    enum Oper {
        /**
         * Func2: (contextKey, contextValue)
         */
        SET(c-> (Action2<String,Object>) c::set),
        /**
         * Func1: (contextKey, contextValue)
         */
        GET(c-> (Func1<String,Object>) c::get),
        /**
         * Result
         */
        RESULT(null);

        Oper(Func1<CallerContext, Object> getFunc) {
            this.getFunc = getFunc;
        }

        public Func1<CallerContext,Object> getFunc;
    }

}
