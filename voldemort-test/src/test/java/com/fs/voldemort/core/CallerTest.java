package com.fs.voldemort.core;

import java.math.BigDecimal;

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

    public void test_TCCCaller() {
        /*

        TccTransaction tcc = new TccTransaction();
        tcc.begin(); // 创建数据库状态

        try {
            while(tcc.next()) {
                TccNode node = tcc.current();
                if(node.try()) {
                    node.invoke();
                }
            }
            tcc.commit();
        } catch(Exception e) {
            tcc.rollback(); // 调用每个节点的rollback方法
        } finally {
            tcc.end(); // 将数据库状态更新为完成
        }

        */
    }
    
}
