package com.fs.voldemort.business.util;

import java.util.Collection;

public interface ParamFinder {

    <T> Collection<ParamFindResult> getParam(T target);

}
