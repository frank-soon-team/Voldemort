package com.fs.voldemort.business;

import com.fs.voldemort.Wand;
import com.fs.voldemort.business.horcruxes.*;
import com.fs.voldemort.business.ioc.AService;
import com.fs.voldemort.business.ioc.BService;
import com.fs.voldemort.business.ioc.CMapper;
import com.fs.voldemort.parallel.ParallelTaskResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BFuncCallerTest {

    private void init() {
        List<Object> HorcruxesList = Arrays.asList(new HutchpatchGoldenCup(), new Nagini(),
                new MarvoroGunterRing(), new RoinaRavenclawCrown(), new TomRyderDiary());
        BFuncManager.init(HorcruxesList);

        final Map<String,?> iocInstanceContainerMap = new HashMap<>(){{
            put("aService", new AService());
            put("bService", new BService());
            put("cMapper", new CMapper());
        }};

        BFuncManager.initIocInstanceGetFunc(clazz->null, iocInstanceContainerMap::get);
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
                            return "-> First call! ";
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

//        Assert.assertEquals("-> First call\n" +
//                "-> call TomRyderDiary\n" +
//                "->Second call!", result);
    }

    /**
     * 第四步，调用链上下文由于其作用是为了承载业务上下文，在多变的业务场景中会存在上下文中的业务参数无法满足逻辑单元参数填充的问题（比如一些配置
     * 或者特殊参数），Voldemort提供了临时参数入口{@link NodeParam}
     */
    @Test
    public void test_NodeParam() {
        init();

        String result =
                Wand.business()
                        .call(p->{
                            p.context().set("Key1","First context value!");
                            return "-> First call";
                        })
                        /*
                         * 链接 TomRyderDiary 逻辑单元
                         */
                        .call(TomRyderDiary.class, new NodeParam("nodeKey1", "nodeValue"))
                        .call(p->{
                            return p.result + "\n->Second call! C:Key2->" + p.context().getString("Key2");
                        })
                        .get()
                        .exec();

        System.out.println(result);
        //Assert.assertEquals("-> First call! \n-> call TomRyderDiary \n-> Second call! C:Key2->TomRyderDiary Key2 value", result);
    }

    /**
     * 第四步，通过一个并行子链，并行地处理父链的请求
     */
    @Test
    public void test_Parallel() {
        init();

        String result =
                Wand.business()
                        .call(p->"->First call!")
                        .call(HutchpatchGoldenCup.class)
                        .call(Nagini.class)
                        /*
                         * 创建子链
                         */
                        .sub()
                        /*
                         * 选择子链类型为parallel
                         */
                        .parallel()
                        /*
                         * 异步线程1
                         */
                        .call(p -> p.result + " Parallel Thread 1:" + Thread.currentThread().getName())
                        /*
                         * 异步线程2
                         */
                        .call(p -> p.result + " Parallel Thread 2:" + Thread.currentThread().getName())
                        /*
                         * 异步线程3
                         */
                        .call(RoinaRavenclawCrown.class)
                        /*
                         * 子链结束
                         */
                        .end()
                        /*
                         * 在子链end后的第一个函数中通过((ParallelTaskResult)p.result).getResult()获取并行结果
                         */
                        .call(p->{
                            /*
                             * 将key为c3的参数放入call链上下文
                             */
                            p.context().set("c3","C3!!!");
                            /*
                             * 第一次 ((ParallelTaskResult)p.result).getResult() 获取异步线程1的处理结果
                             */
                            System.out.println("p1 call result: " + ((ParallelTaskResult)p.result).getResult());
                            /*
                             * 第二次 ((ParallelTaskResult)p.result).getResult() 获取异步线程1的处理结果
                             */
                            System.out.println("p2 call result: " + ((ParallelTaskResult)p.result).getResult());
                            /*
                             * 第二次 ((ParallelTaskResult)p.result).getResult() 获取异步线程2的处理结果
                             */
                            return "p3 call result: " + ((ParallelTaskResult)p.result).getResult();
                        })
                        .get().exec();
    }

    /**
     * 一个比较综合的调用链
     */
    @Test
    public void test_LongParallelCaller() {
        init();

        String result =
                Wand.business()
                        .call(p->"Begin!")
                        .call(HutchpatchGoldenCup.class)
                        .call(Nagini.class)
                        .sub().parallel()
                        .sub().parallel()
                        .call(p->p.result + "Sub Parallel Thread1:" + Thread.currentThread().getName())
                        .call(p->p.result + "Sub Parallel Thread2:" + Thread.currentThread().getName())
                        .end()
                        .call(RoinaRavenclawCrown.class)
                        .end()
                        .call(p->{
                            p.context().set("c3","C3!!!");
                            ParallelTaskResult p1 = (ParallelTaskResult)((ParallelTaskResult)p.result).getResult();
                            String p2 = ((ParallelTaskResult)p.result).getResult().toString();

                            System.out.println("SP1 call result: " + p1.getResult());
                            System.out.println("SP2 call result: " + p1.getResult());
                            return "\nRoinaRavenclawCrown Parallel call result: " + p2;
                        })
                        /*
                         * 调用MarvoroGunterRing逻辑单元
                         */
                        .call(MarvoroGunterRing.class)
                        .get()
                        .exec();

        System.out.println(result);
    }


    /**
     * A whole new one function invoke mode
     */
    @Test
    public void  test_FunnyOne() {
        /*
         * 手动初始化函数库
         */
        init();

        String result =
            Wand
                .business()
                .call(p->{
                    p.context().set("Key1","First context value!");
                    return "-> First call";
                })
                .callFitly((AService aService, CMapper cMapper)->{
                    aService.aMethod();
                    cMapper.cMethod();
                    return null;
                })
                .get()
                .exec();

        System.out.println(result);
    }
}
