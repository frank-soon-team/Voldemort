package com.fs.voldemort.business.support;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@BFuncHorcruxes
public @interface LogicCell {
}
