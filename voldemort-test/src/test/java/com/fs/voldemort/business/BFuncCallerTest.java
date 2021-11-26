package com.fs.voldemort.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs.voldemort.Wand;
import com.fs.voldemort.business.horcruxes.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BFuncCallerTest {

    private void init() {
        List<Object> HorcruxesList = Arrays.asList(new HutchpatchGoldenCup(), new Nagini(),
                new MarvoroGunterRing(), new RoinaRavenclawCrown(), new TomRyderDiary());
        BFuncManager.init(HorcruxesList);
    }

    /**
     * 第一步，创建一个基础Call链，并执行
     */
    @Test
    public void test_helloVoldemort() {
        String result =
            Wand
                /*
                 * 创建基础Call链实例
                 */
                .caller()
                /*
                 * call函数，放业务逻辑，第一个call一般用于处理请求参数
                 * 参数说明：p {@link com.fs.voldemort.core.support.CallerParameter}
                 */
                .call(p -> "-> First call! ")
                /*
                 * 通过p.result获取上个call处理结果
                 */
                .call(p -> p.result + "\n-> Second call!")
                /*
                 * 获取call链实例
                 */
                .get()
                /*
                 * 执行call链
                 */
                .exec();

        Assert.assertEquals("-> First call! \n-> Second call!", result);
    }

    /**
     * 第二步，创建一个简单的基础call链，并在链上下文中放入自定参数
     */
    @Test
    public void test_Context() {
        String result =
            Wand
                .caller()
                .call(p->{
                    /*
                     * 写入上下文
                     */
                    p.context().set("Key1","First context value!");
                    return "-> First call";
                })
                .call(p->{
                    /*
                     * 获取上下文
                     */
                    String context1 = p.context().getString("Key1");
                    return p.result + "\n-> Second call! Context key1 value:" + context1 ;
                })
                .get()
                .exec();

        Assert.assertEquals("-> First call! \n-> Second call! Context key1 value:First context value!", result);
    }

    /**
     * 第三步，创建一个简单的Business call链
     * Business链与基础链的不同点是Business链引入了逻辑单元，而逻辑单元是高度独立，可复用、参数自动匹配、支持DI容器的独立函数
     * Business链是对基础链的增强，向下兼容
     */
    @Test
    public void test_Business(){
        /*
         * 手动初始化函数库
         */
        init();

        String result =
            Wand
                /*
                 * 创建基础Call链实例
                 */
                .business()
                .call(p->{
                    p.context().set("Key1","First context value!");
                    return "-> First call";
                })
                /*
                 * 链接 TomRyderDiary 逻辑单元
                 */
                .call(TomRyderDiary.class)
                .call(p->{
                    System.out.println(p.context().getString("Key2"));
                    return p.result + "\n->Second call!";
                })
                .get()
                .exec();

        Assert.assertEquals("-> First call! \n-> Second call! Context key1 value:First context value!", result);
    }



    @Test
    public void test_BusinessCaller() {
        init();

        String result = BFuncCaller.create()
                .call(p-> "Begin!")
                .call(HutchpatchGoldenCup.class)
                .call(Nagini.class)
                .call(p->{
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(p));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    p.context().set("c3","C3!!!");
                    return "Result 666";
                })
                .call(MarvoroGunterRing.class)
                .exec();

        System.out.println(result);
    }

}
