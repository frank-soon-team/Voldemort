package com.fs.voldemort.core;

import java.math.BigDecimal;
import java.util.Map;

import com.fs.voldemort.core.support.CallerContext;

import org.junit.Assert;
import org.junit.Test;

public class CallerTest {

    @Test
    public void test_Caller() {
        String result = new Caller<String>()
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
                new Caller<String>()
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
        new Caller<Integer>()
            .call(p -> 1)
            .call(p -> ((Integer) p.result) + 1)
            .call(p -> new Caller<Integer>(p).call(p2 -> ((Integer) p2.result) + 2))
            .exec(r -> Assert.assertTrue(Integer.valueOf(4).equals(r)));
    }

    @Test
    public void test_contextFunctional() {
        new Caller<>()
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

    @Test
    public void test_contextFunctionalWithValue() {
        new Caller<>()
            .call(p -> 1)
            .call(p -> {
                CallerContext context = p.context();
                context.set("addValue", 100);
                context.declareFunction("add", (Integer a) -> a + context.getInteger("addValue"));
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                context.set("subtractValue", 1);
                context.declareFunction("subtract", (Integer a) -> a - context.getInteger("subtractValue"));
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                context.set("multipleValue", 2);
                context.declareFunction("multiple", (Integer a) -> a * context.getInteger("multipleValue"));
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                context.set("divideValue", 50);
                context.declareFunction("divide", (Integer a) -> a / context.getInteger("divideValue"));
                return p.result;
            })
            .call(p -> {
                CallerContext context = p.context();
                Integer value = (Integer) p.result;
                value = context.callFunction("add", value); // 101
                value = context.callFunction("subtract", value); // 100
                value = context.callFunction("multiple", value); // 200
                value = context.callFunction("divide", value); // 4

                return value;
            })
            .exec(r -> Assert.assertEquals(r, 4));
    }

    @Test
    public void test_Context() {
        CallerContext context = new Caller<CallerContext>()
            .call(p -> {
                p.context().set("name", "Jack");
                p.context().set("age", 18);
                p.context().set("gender", "male");
                return 1;
            })
            .call(p -> null)
            .call(p -> null)
            .call(p -> null)
            .call(p -> {
                p.context().set("alias", "Thon");
                return p.context();
            })
            .exec();

        Map<String, Object> contextMap = context.getValueMap();
        CallerContext newContext = new CallerContext(contextMap);
        Assert.assertTrue(contextMap.toString().equals(newContext.getValueMap().toString()));
    }

    @Test
    public void test_ContextWithParent() {

        CallerContext context = new Caller<CallerContext>()
            .call(p -> {
                p.context().set("name", "Jack");
                p.context().set("age", 18);
                p.context().set("gender", "male");
                return 1;
            })
            .call(p -> new Caller<>(p, true).call(p1 -> { p1.context().set("score", 99); return p1.result; }))
            .call(
                new Caller<>()
                    .call(p -> {
                        p.context().set("alias", "Thon");
                        return p.context();
                    })
            )
            .call(p -> p.result)
            .call(p -> {
                p.context().set("alias", "Thon");
                return p.result;
            })
            .exec();

        Map<String, Object> contextMap = context.getValueMap();
        System.out.println(contextMap);
        CallerContext newContext = new CallerContext(contextMap);
        Assert.assertTrue(contextMap.toString().equals(newContext.getValueMap().toString()));

    }
    
}
