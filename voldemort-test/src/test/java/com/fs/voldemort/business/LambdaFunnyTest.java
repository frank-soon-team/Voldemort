package com.fs.voldemort.business;

import com.fs.voldemort.business.fit.ContainerOnly;
import com.fs.voldemort.business.fit.ContextOnly;
import com.fs.voldemort.business.fit.Default;
import com.fs.voldemort.business.paramfinder.ParamFindResult;
import com.fs.voldemort.business.paramfinder.ParamFinderLibrary;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.action.Action2;
import org.junit.Test;

import java.util.Collection;

public class LambdaFunnyTest {

    @Test
    public void test_Lambda() {
        Action2<?,?> b = (@ContextOnly @Default String s1, @ContainerOnly String s2) -> {
            System.out.println(s1);
        };

        Collection<ParamFindResult> params = ParamFinderLibrary.f_LambdaParamFinder.getParam(b);
        System.out.println(params);

    }
}
