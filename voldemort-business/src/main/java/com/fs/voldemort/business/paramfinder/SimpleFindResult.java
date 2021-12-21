package com.fs.voldemort.business.paramfinder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The result which is hold name, class and annotation that belong args of method
 */
public class SimpleFindResult implements ParamFindResult {

    /**
     * The name of args
     */
    private final String name;

    /**
     * The class of args
     */
    private final Class<?> clazz;

    /**
     * The annotations of args
     */
    private final Collection<Annotation> annotations;

    public SimpleFindResult(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
        this.annotations = new ArrayList<>();
    }

    public SimpleFindResult(String name, Class<?> clazz, Collection<Annotation> annotations) {
        this.name = name;
        this.clazz = clazz;
        this.annotations = annotations;
    }

    @Override
    public String getParamName() {
        return this.name;
    }

    @Override
    public Class<?> getParamClazz() {
        return this.clazz;
    }

    @Override
    public Collection<Annotation> getAnnotation() {
        return annotations;
    }
}
