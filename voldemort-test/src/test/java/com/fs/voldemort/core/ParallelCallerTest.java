package com.fs.voldemort.core;

import com.fs.voldemort.Wand;
import com.fs.voldemort.parallel.ParallelTaskResult;

import org.junit.Test;

public class ParallelCallerTest {

    @Test
    public void test_ParallelCaller() {
        long time = System.currentTimeMillis();
        Object result = Wand.parallelCaller()
                .call(p -> {
                    sleep(2000L);
                    return 1;
                })
                .call(p -> {
                    sleep(2000L);
                    return 2;
                })
                .call(p -> {
                    sleep(2000L);
                    return 3;
                })
                .exec();
        System.out.println(System.currentTimeMillis() - time);

        ParallelTaskResult parallelTaskResult = (ParallelTaskResult) result;
        System.out.println(parallelTaskResult.getResult());
        System.out.println(parallelTaskResult.getResult());
        System.out.println(parallelTaskResult.getResult());
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
    
}
