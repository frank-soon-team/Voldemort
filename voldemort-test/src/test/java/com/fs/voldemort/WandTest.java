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

    @Test
    public void test_intoContext() {
        Integer result = Wand.caller()
            .into("num1", 1)
            .call(p -> 1)
            .into("num2", 1)
            .into("num3", 1)
            .call(p -> {
                Integer num1 = p.context().getInteger("num1");
                Integer num2 = p.context().getInteger("num2");
                Integer num3 = p.context().getInteger("num3");
                return ((Integer) p.result) + num1 + num2 + num3;
            })
            .exec();
        Assert.assertTrue(result.intValue() == 4);
    }
    
}
