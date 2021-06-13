package com.fs.voldemort.core.business;

import com.fs.voldemort.business.BFuncCaller;
import com.fs.voldemort.core.business.horcruxes.HutchpatchGoldenCup;
import com.fs.voldemort.core.business.horcruxes.MarvoroGunterRing;
import com.fs.voldemort.core.business.horcruxes.Nagini;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BFuncCallerTest {

    private void init() {
        List<Object> HorcruxesList = Arrays.asList(new HutchpatchGoldenCup(),new Nagini(),new MarvoroGunterRing());
        BFuncCaller.init(HorcruxesList);
    }

    @Test
    public void test_BusinessCaller() {
        init();

        String result = BFuncCaller.create()
                .call(p-> "Begin!")
                .call(HutchpatchGoldenCup.class)
                .call(Nagini.class)
                .call(p->{
                    p.context().set("c3","C3!!!");
                    return "Result 666";
                })
                .call(MarvoroGunterRing.class)
                .exec();

        System.out.println(result);
    }

}
