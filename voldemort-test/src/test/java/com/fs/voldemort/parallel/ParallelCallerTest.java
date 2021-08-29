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
        Object result = ParallelCaller.create()
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

        ParallelTaskResult parallelTaskResult = (ParallelTaskResult) result;
        Assert.assertTrue(Integer.valueOf(1).equals(parallelTaskResult.getResult()));
        Assert.assertTrue(Integer.valueOf(2).equals(parallelTaskResult.getResult()));
        Assert.assertTrue(Integer.valueOf(3).equals(parallelTaskResult.getResult()));
    }

    @Test
    public void test_CallerWithParallel() {
        Object result = Caller.create()
            .call(p -> 1)
            .call(p -> new BigDecimal("3").add(new BigDecimal(p.result.toString())))
            .call(
                ParallelCaller.create()
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
