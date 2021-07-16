package com.fs.voldemort.core;

import java.math.BigDecimal;

import com.fs.voldemort.Wand;
import com.fs.voldemort.core.support.CallerContext;

import org.junit.Assert;
import org.junit.Test;

public class CallerTest {

    @Test
    public void test_Caller() {
        String result = Caller
            .create()
            .call(p -> {
                System.out.println("call1:" + String.valueOf(p.result));
                p.context().set("c_key","c_value1");
                return new BigDecimal("1");
            })
            .call(p -> {
                System.out.println("call2: " + p.result);
                p.context().set("c_num_key", new BigDecimal("2"));
                return ((BigDecimal)p.result).add(new BigDecimal("1"));
            })
            .call(
                Caller
                    .create()
                    .call(p -> {
                        BigDecimal num = (BigDecimal) p.context().get("c_num_key");
                        System.out.println("c_num_key: " + num);
                        return null;
                    })
                    .call(p -> {
                        p.context().update("c_num_key", new BigDecimal("3"));
                        return null;
                    })
                    .call(p -> "Hello")
                    .call(p -> p.result + " World !")
            )
            .call(p -> {
                System.out.println("call3: " + p.result);
                System.out.println("p.context.c_key: " + p.context().get("c_key"));
                System.out.println("p.context.c_num_key: " + p.context().get("c_num_key"));
                return "success";
            })
            .exec();

        Assert.assertTrue(result.equals("success"));
    }

    @Test
    public void test_dynamicCaller() {
        Wand.caller()
            .call(p -> 1)
            .call(p -> ((Integer) p.result) + 1)
            .call(p -> Wand.caller(p).call(p2 -> ((Integer) p2.result) + 2))
            .exec(r -> Assert.assertTrue(Integer.valueOf(4).equals(r)));
    }

    @Test
    public void test_contextFunctional() {
        Wand.caller()
            .call(p -> 1)
            .call(p -> {
                CallerContext context = p.context();
                context.declareFunction("add", (Integer a, Integer b) -> a + b);
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                context.declareFunction("subtract", (Integer a, Integer b) -> a - b);
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                context.declareFunction("multiple", (Integer a, Integer b) -> a * b);
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                context.declareFunction("divide", (Integer a, Integer b) -> a / b);
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                Integer value = (Integer) p.result;
                value = context.callFunction("add", value, 100); // 101
                value = context.callFunction("subtract", value, 1); // 100
                value = context.callFunction("multiple", value, 2); // 200
                value = context.callFunction("divide", value, 50); // 4

                return value;
            })
            .exec(r -> Assert.assertEquals(r, 4));
    }

    public void test_BusinessCaller() {
        // BusinessCaller.create().call();

        // TccCaller.create().call();

        /*

            // Lambda调用链
            Wand.caller().call().exec();
            // 函数调用链
            Wand.businessCaller().call().exec();
            // TCC事务链
            Wand.tccCaller().call().exec();

            // 多事务链
            Wand.caller()
                .call(
                    Voldmort.tccCaller().call(ITCCNode.class)
                )
                .call(
                    Voldmort.tccCaller().call(ITCCNode.class)
                )
                .exec();
        
        */

        // CallerFactory.businessCaller().call();

        // CallerFactory.tccCaller().call();

    }
    
}
