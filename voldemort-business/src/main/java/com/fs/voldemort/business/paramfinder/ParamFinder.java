package com.fs.voldemort.business.paramfinder;

import java.util.Collection;

@FunctionalInterface
public interface ParamFinder {
    <T> Collection<ParamNameTypeFindResult> getParam(T target) throws ParamFinderException;
}
