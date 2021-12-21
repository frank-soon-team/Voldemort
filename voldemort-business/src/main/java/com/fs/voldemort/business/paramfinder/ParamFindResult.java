package com.fs.voldemort.business.paramfinder;
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
}
