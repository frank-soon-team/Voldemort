package com.fs.voldemort.business.fit;

import com.fs.voldemort.business.paramfinder.ParamFindResult;
import com.fs.voldemort.business.paramfinder.ParamFinderException;

@FunctionalInterface
public interface FitArg{
    Object getParam(ParamFindResult paramFindResult, FitContext fitContext) throws ParamFinderException;
}
