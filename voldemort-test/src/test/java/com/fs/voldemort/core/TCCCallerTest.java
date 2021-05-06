package com.fs.voldemort.core;
 

import com.fs.voldemort.Wand;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.node.BaseTCCHandler;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.simple.SimpleTCCManager;
import com.fs.voldemort.tcc.state.ITCCState;

import org.junit.Test;

public class TCCCallerTest {
    
    @Test
    public void test_TCCCaller() {
        Wand.tccCaller(new SimpleTCCManager())
            .call(
                createHandler(
                    "Coupon", 
                    p -> { System.out.println("预留优惠券"); return true; },
                    s -> { System.out.println("扣减优惠券"); },
                    s -> System.out.println("返回优惠券"))
            )
            .call(
                createHandler(
                    "Point", 
                    p -> { System.out.println("计算奖励积分"); return true; },
                    s -> { System.out.println("积分奖励生效"); },
                    s -> System.out.println("积分奖励取消"))
            )
            .call(
                createHandler(
                    "Gift", 
                    p -> { System.out.println("预留赠品"); return true; },
                    s -> { System.out.println("赠品赠送"); },
                    s -> System.out.println("赠品取消"))
            )
            .exec();

    }

    // public void test_TCC_Rollback() {
    //     int[] value = new int[] { 0 };
    //     Wand.tccCaller(new SimpleTCCManager())
    //         .call(
    //             createHandler(
    //                 "Step1", 
    //                 p -> p, 
    //                 () -> null, 
    //                 () -> null)
    //         )
    //         .exec();
    // }

    public ITCCHandler createHandler(String name, Func1<CallerParameter, Object> goTry, Action1<ITCCState> confirm, Action1<ITCCState> cancel) {
        return new BaseTCCHandler(name) {

            @Override
            public Object goTry(CallerParameter parameter) {
                return goTry.call(parameter);
            }

            @Override
            public void confirm(ITCCState tccState) {
                confirm.apply(tccState);
            }

            @Override
            public void cancel(ITCCState tccState) {
                cancel.apply(tccState);
            }
            
        };
    }

}
