package com.fs.voldemort.core;
 

import com.fs.voldemort.Wand;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.TCCCaller;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.node.BaseTCCHandler;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.simple.SimpleTCCManager;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;
import com.fs.voldemort.tcc.state.ITCCState;

import org.junit.Assert;
import org.junit.Test;

public class TCCCallerTest {
    
    @Test
    public void test_TCCCaller() {
        Wand.tccCaller(buildMananger())
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

    @Test
    public void test_TCC_Success() {
        int[] value = new int[] { 0 };
        TCCCaller tccCaller = Wand.tccCaller(buildMananger(), value);
        tccCaller
            .call(
                createHandler(
                    "Step1", 
                    p -> {
                        int[] result = (int[]) ((TCCNodeParameter) p).getTCCState().getParam();
                        result[0] += 1;
                        return null;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] += 1;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        if(result[0] > 0) {
                            result[0] -= 1;
                        }
                    })
            )
            .call(
                createHandler(
                    "Step2", 
                    p -> {
                        int[] result = (int[]) ((TCCNodeParameter) p).getTCCState().getParam();
                        result[0] += 1;
                        return null;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] += 1;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] -= 1;
                    })
            )
            .call(
                createHandler(
                    "Step3", 
                    p -> {
                        int[] result = (int[]) ((TCCNodeParameter) p).getTCCState().getParam();
                        result[0] += 1;
                        return null;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] += 1;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] -= 1;
                    })
            );

        tccCaller.exec();

        Assert.assertTrue(value[0] == 6);
    }

    @Test
    public void test_TCC_Rollback() {
        int[] value = new int[] { 0 };
        TCCCaller tccCaller = Wand.tccCaller(buildMananger(), value);
        tccCaller
            .call(
                createHandler(
                    "Step1", 
                    p -> {
                        int[] result = (int[]) ((TCCNodeParameter) p).getTCCState().getParam();
                        result[0] += 1;
                        return result;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] += 1;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        if(result[0] > 0) {
                            result[0] -= 1;
                        }
                    })
            )
            .call(
                createHandler(
                    "Step2", 
                    p -> {
                        int[] result = (int[]) p.result;
                        result[0] += 1;
                        return result;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] += 1;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] -= 1;
                    })
            )
            .call(
                createHandler(
                    "Step3", 
                    p -> {
                        throw new IllegalStateException("throw");
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] += 1;
                    }, 
                    s -> {
                        int[] result = (int[]) s.getParam();
                        result[0] -= 1;
                    })
            );

        try {
            tccCaller.exec();
        } catch(ExecuteCallerNodeException e) {
            System.out.println("Rollback > " + e.getMessage());
        }

        Assert.assertTrue(value[0] == 0);
    }

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

    public SimpleTCCManager buildMananger() {

        return SimpleTCCManager.builder()
                .setRepositoryGear(new IRepositoryGear() {

                    @Override
                    public boolean create(TCCTaskModel taskModel) {
                        System.out.println("create TCCTaskModel");
                        return true;
                    }

                    @Override
                    public boolean update(TCCTaskModel taskModel) {
                        System.out.println("update TCCTaskModel");
                        return true;
                    }
                    
                })
                .setSerializeGear(new ISerializeGear(){
                    @Override
                    public String serialize(Object obj) {
                        return null;
                    }

                    @Override
                    public <T> T deserialize(String serializeStr) {
                        return null;
                    }
                    
                })
                .setBusinessSupportGear(null)
                .build();


        // return SimpleTCCManager.extendBuilder()
        //     .setTCCBeginBiz(new SimpleTCCBeginBiz())
        //     .setTCCUpdateBiz(new SimpleTCCUpdateBiz())
        //     .setTCCEndBiz(new SimpleTCCEndBiz())
        //     .setTCCConfirmRetryBiz(new SimpleTCCConfirmRetryBiz())
        //     .setTCCCancelRetryBiz(new SimpleTCCCancelRetryBiz())
        //     .build();
    }

}
