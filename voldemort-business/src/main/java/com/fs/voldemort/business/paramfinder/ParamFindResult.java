package com.fs.voldemort.business.paramfinder;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * The result of parameter finder{@link ParamFinder}
 */
public interface ParamFindResult {

    /**
     * Parameter name
     * @return Parameter name
     */
    String getParamName();

    /**
     * Parameter class
     * @return Parameter class
     */
    Class<?> getParamClazz();

    /**
     * Parameter annotation
     * @return Parameter annotation
     */
    Collection<Annotation> getAnnotation();
}
