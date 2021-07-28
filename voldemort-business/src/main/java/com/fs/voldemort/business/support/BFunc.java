package com.fs.voldemort.business.support;

import com.fs.voldemort.business.fit.FitMode;
import com.fs.voldemort.business.fit.IFit;

import java.lang.annotation.*;

/**
 * @author frank
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BFunc {

    FitMode fit() default FitMode.AUTO;

    Class<? extends IFit> iFit() default IFit.class;

}
