package com.fs.voldemort;

import java.math.BigDecimal;

import com.fs.voldemort.parallel.ParallelTaskResult;
import com.fs.voldemort.utilies.ThreadUtils;

import org.junit.Assert;
import org.junit.Test;

public class WandTest {

    @Test
    public void test_wandBasic() {
        Object result = Wand.caller()
            .call(p -> 1)
            .call(p -> new BigDecimal("3").add(new BigDecimal(p.result.toString())))
            .sub()
                .parallel()
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
                    .end()
            .call(p -> {
                ParallelTaskResult parallelTaskResult = (ParallelTaskResult) p.result;
                return new BigDecimal(parallelTaskResult.getResult().toString())
                    .add(new BigDecimal(parallelTaskResult.getResult().toString()))
                    .add(new BigDecimal(parallelTaskResult.getResult().toString()))
                    .add(new BigDecimal(parallelTaskResult.getResult().toString()));
            })
            .get()
            .exec();

        Assert.assertTrue(((BigDecimal)result).equals(new BigDecimal("10")));
    }
    
}