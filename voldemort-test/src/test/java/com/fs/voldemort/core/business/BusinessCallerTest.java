package com.fs.voldemort.core.business;

import com.fs.voldemort.business.BusinessCaller;
import com.fs.voldemort.core.business.horcruxes.HutchpatchGoldenCup;
import com.fs.voldemort.core.business.horcruxes.MarvoroGunterRing;
import com.fs.voldemort.core.business.horcruxes.Nagini;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class BusinessCallerTest {

    private void init() {
        List<Object> HorcruxesList = Arrays.asList(new HutchpatchGoldenCup(),new Nagini(),new MarvoroGunterRing());
        BusinessCaller.init(HorcruxesList);
    }

    @Test
    public void test_BusinessCaller() {
        init();

        String result = BusinessCaller.create()
                .call(p-> "Begin!")
                .call(HutchpatchGoldenCup.class)
                .call(Nagini.class)
                .call(MarvoroGunterRing.class)
                .exec();

        System.out.println(result);
    }

}
