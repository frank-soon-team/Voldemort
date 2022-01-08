package com.fs.voldemort.parallel;

import java.math.BigDecimal;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.utilies.ThreadUtils;

import org.junit.Assert;
import org.junit.Test;

public class ParallelCallerTest {

    @Test
    public void test_ParallelCaller() {
        long time = System.currentTimeMillis();
        ParallelTaskResult result = new ParallelCaller()
                .call(p -> {
                    ThreadUtils.sleep(2000L);
                    return 1;
                })
                .call(p -> {
                    ThreadUtils.sleep(2000L);
                    return 2;
                })
                .call(p -> {
                    ThreadUtils.sleep(2000L);
                    return 3;
                })
                .exec();

        long expendTime = System.currentTimeMillis() - time;
        Assert.assertTrue(expendTime < 6000L);

        Assert.assertTrue(Integer.valueOf(1).equals(result.getResult()));
        Assert.assertTrue(Integer.valueOf(2).equals(result.getResult()));
        Assert.assertTrue(Integer.valueOf(3).equals(result.getResult()));
    }

    @Test
    public void test_CallerWithParallel() {
        BigDecimal result = new Caller<BigDecimal>()
            .call(p -> 1)
            .call(p -> new BigDecimal("3").add(new BigDecimal(p.result.toString())))
            .call(
                new ParallelCaller()
                    .call(p -> p.result)
                    .call(p -> {
                        ThreadUtils.sleep(3000L);
                        return 2;
                    })
                    .call(p -> {
                        ThreadUtils.sleep(3000L);
                        return 2;
                    })
                    .call(p -> {
                        ThreadUtils.sleep(3000L);
                        return 2;
                    })
            )
            .call(p -> {
                ParallelTaskResult parallelTaskResult = (ParallelTaskResult) p.result;
                return new BigDecimal(parallelTaskResult.getResult().toString())
                    .add(new BigDecimal(parallelTaskResult.getResult().toString()))
                    .add(new BigDecimal(parallelTaskResult.getResult().toString()))
                    .add(new BigDecimal(parallelTaskResult.getResult().toString()));
            })
            .exec();

        Assert.assertTrue(((BigDecimal)result).equals(new BigDecimal("10")));
    }
    
}
