package com.fs.voldemort.business.horcruxes;

import com.fs.voldemort.business.support.BFunc;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.business.support.LogicCell;
import com.fs.voldemort.core.functional.action.Action2;

/**
 * 逻辑单元需要申明此注解
 */
@LogicCell
public class TomRyderDiary {

    /**
     * 逻辑单元函数需要在对应函数上申明此注解{@link BFunc}
     * 方法参数通过自动匹配进行传递，提供默认自动匹配模式，在特殊场景下支持自动移匹配器{@link HutchpatchGoldenCup}
     * 在默认自动匹配模式下，根据参数位置与标识函数将方法参数分为两种类型：
     *    1.可自动进行匹配的参数,参数位置为[1-N]
     *      逻辑单元会将方法中待匹配参数，按参数名称去调用链上下文中进行匹配
     *    2.操作函数,参数位置为[2-N]
     *      操作函数提供了参数外的操作入口，可以通过操作函数读取{@link BFuncOperate.Oper.GET}或
     *      更新{@link BFuncOperate.Oper.SET}上下文，也可以获取上个函数返回的结果{@link BFuncOperate.Oper.RESULT}
     *
     * @param nodeKey1  第一种类型的参数，按参数名称'nodeKey1'去上下文中进行匹配
     * @param Key1      第一种类型的参数，按参数名称'Key1'去上下文中进行匹配
     * @param f_setC    第二种类型的参数，提供上下文SET操作
     * @param result    第二种类型的参数，提供获取上个函数RESULT的函数
     * @return 逻辑单元结果
     */
    @BFunc
    public String func(String nodeKey1, String Key1,
                       @BFuncOperate(BFuncOperate.Oper.SET) Action2<String,Object> f_setC,
                       @BFuncOperate(BFuncOperate.Oper.RESULT) String result) {

        System.out.println("[TomRyderDiary] Key1:" + Key1 + " nodeKey1:" + nodeKey1);
        System.out.println("\n [TomRyderDiary] Result:" + result);

        f_setC.apply("Key2","TomRyderDiary Key2 value");

        return result + "\n-> call TomRyderDiary";
    }

}
