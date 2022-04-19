package com.fs.voldemort.tcc;
 
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.simple.SimpleTCCManager;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCStateBiz;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;
import com.fs.voldemort.tcc.state.ITCCState;

import org.junit.Assert;
import org.junit.Test;

public class TCCCallerTest {
    
    @Test
    public void test_TCCCaller() {
        new TCCCaller(buildMananger())
            .call(
                "Coupon",
                createHandler( 
                    p -> { System.out.println("预留优惠券"); },
                    p -> { System.out.println("扣减优惠券"); },
                    p -> { System.out.println("返回优惠券"); })
            )
            .call(
                "Point", 
                createHandler(
                    p -> { System.out.println("计算奖励积分"); },
                    p -> { System.out.println("积分奖励生效"); },
                    p -> { System.out.println("积分奖励取消"); })
            )
            .call(
                "Gift", 
                createHandler(
                    p -> { System.out.println("预留赠品"); },
                    p -> { System.out.println("赠品赠送"); },
                    p -> { System.out.println("赠品取消"); })
            )
            .exec();

    }

    @Test
    public void test_TCC_Success() {
        int[] value = new int[] { 0 };
        TCCCaller tccCaller = new TCCCaller(buildMananger(), value);
        tccCaller
            .call(
                "Step1", 
                createHandler(
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        if(result[0] > 0) {
                            result[0] -= 1;
                        }
                    })
            )
            .call(
                "Step2", 
                createHandler(
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] -= 1;
                    })
            )
            .call(
                "Step3", 
                createHandler(
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] -= 1;
                    })
            );

        tccCaller.exec();

        Assert.assertTrue(value[0] == 6);
    }

    @Test
    public void test_TCC_Rollback() {
        int[] value = new int[] { 0 };
        TCCCaller tccCaller = new TCCCaller(buildMananger(), value);
        tccCaller
            .call(
                "Step1", 
                createHandler(
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        if(result[0] > 0) {
                            result[0] -= 1;
                        }
                    })
            )
            .call(
                "Step2", 
                createHandler(
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] -= 1;
                    })
            )
            .call(
                "Step3", 
                createHandler(
                    p -> {
                        throw new IllegalStateException("throw");
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
                        result[0] += 1;
                    }, 
                    p -> {
                        int[] result = (int[]) p.getTCCState().getParam();
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

    public ITCCHandler createHandler(Action1<TCCNodeParameter> goTry, Action1<TCCNodeParameter> confirm, Action1<TCCNodeParameter> cancel) {
        return new ITCCHandler() {

            @Override
            public void goTry(TCCNodeParameter parameter) {
                goTry.apply(parameter);
            }

            @Override
            public void confirm(TCCNodeParameter parameter) {
                confirm.apply(parameter);
            }

            @Override
            public void cancel(TCCNodeParameter parameter) {
                cancel.apply(parameter);
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

                    @Override
                    public TCCTaskModel get(String tccTransactionId) {
                        return null;
                    }
                    
                })
                .setSerializeGear(new ISerializeGear() {
                    @Override
                    public String serialize(Object obj) {
                        return null;
                    }

                    @Override
                    public Object deserialize(String serializeStr) {
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

    public void loadTCCExecuteState() {
        String tccTransactionId = "";
        SimpleTCCStateBiz tccStateBiz = new SimpleTCCStateBiz(null, null);
        ITCCState tccState = tccStateBiz.call(tccTransactionId);
    }

}
