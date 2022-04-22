package com.fs.voldemort.tcc;
 
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.simple.SimpleTCCManager;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTask;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskNode;
import com.fs.voldemort.tcc.state.TCCPhase;
import com.fs.voldemort.tcc.state.TCCStatus;
import com.fs.voldemort.tcc.state.TCCTaskStatus;

import org.junit.Assert;
import org.junit.Test;

public class TCCCallerTest {
    
    @Test
    public void test_TCCCaller() {
        Void result = new TCCCaller(buildMananger())
            .call(
                "Coupon",
                p -> { System.out.println("预留优惠券"); },
                p -> { System.out.println("扣减优惠券"); },
                p -> { System.out.println("返回优惠券"); }
            )
            .call(
                "Point", 
                p -> { System.out.println("计算奖励积分"); },
                p -> { System.out.println("积分奖励生效"); },
                p -> { System.out.println("积分奖励取消"); }
            )
            .call(
                "Gift", 
                p -> { System.out.println("预留赠品"); },
                p -> { System.out.println("赠品赠送"); },
                p -> { System.out.println("赠品取消"); }
            )
            .exec();

        assert result == null;
    }

    @Test
    public void test_TCC_Success() {
        int[] value = new int[] { 0 };
        TCCCaller tccCaller = new TCCCaller(buildMananger(), value);
        tccCaller
            .call(
                "Step1", 
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
                }
            )
            .call(
                "Step2", 
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
                }
            )
            .call(
                "Step3", 
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
                }
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
                }
            )
            .call(
                "Step2", 
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
                }
            )
            .call(
                "Step3", 
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
                }
            );

        try {
            tccCaller.exec();
        } catch(Exception e) {
            System.out.println("Rollback > " + e.getMessage());
            //e.printStackTrace();
        }

        Assert.assertTrue(value[0] == 0);
    }

    @Test
    public void test_confirmCompensation() {
        int[] value = new int[] { 0, 0, 0 };
        TCCCaller tccCaller = new TCCCaller(buildCompensationManager(TCCStatus.ConfirmFailed));
        tccCaller.call(
            "Node1", 
            p -> { throw new IllegalStateException("补偿不会执行Try阶段"); }, 
            p -> value[0] = 1,
            p -> { throw new IllegalStateException("补偿不会执行Cancel阶段"); });
        tccCaller.call(p -> 2);
        tccCaller.call(
            "Node2", 
            p -> { throw new IllegalStateException("补偿不会执行Try阶段"); }, 
            p -> value[1] = 1,
            p -> { throw new IllegalStateException("补偿不会执行Cancel阶段"); });
        tccCaller.call(
            "Node3", 
            p -> { throw new IllegalStateException("补偿不会执行Try阶段"); }, 
            p -> value[2] = 1,
            p -> { throw new IllegalStateException("补偿不会执行Cancel阶段"); });
        tccCaller.exec();

        assert value[0] == 1;
        assert value[1] == 1;
        assert value[2] == 1;
    }

    @Test
    public void test_cancelCompensation() {
        int[] value = new int[] { 1, 1, 1 };
        TCCCaller tccCaller = new TCCCaller(buildCompensationManager(TCCStatus.CancelFailed));
        tccCaller.call(
            "Node1", 
            p -> { throw new IllegalStateException("补偿不会执行Try阶段"); }, 
            p -> { throw new IllegalStateException("补偿不会执行Confrim阶段"); },
            p -> value[0] = 0);
        tccCaller.call(
            "Node2", 
            p -> { throw new IllegalStateException("补偿不会执行Try阶段"); }, 
            p -> { throw new IllegalStateException("补偿不会执行Confirm阶段"); },
            p -> value[1] = 0);
        tccCaller.call(
            "Node3", 
            p -> { throw new IllegalStateException("补偿不会执行Try阶段"); }, 
            p -> { throw new IllegalStateException("补偿不会执行Confirm阶段"); },
            p -> value[2] = 0);
        tccCaller.exec();

        assert value[0] == 0;
        assert value[1] == 0;
        assert value[2] == 0;
    }

    @Test
    public void test_tccNodeResult() {
        int[] value = new int[] { 0 };
        TCCCaller tccCaller = new TCCCaller(buildMananger(), value);
        tccCaller.call(p -> ((TCCNodeParameter) p).getTCCState().getParam());
        tccCaller.call(
                "Step1",
                p -> { 
                    int[] param = p.castResult();
                    param[0]++;
                    p.setResult(param);
                },
                p -> { 
                    throw new IllegalStateException("Not Support Cancel");
                }
            )
            .call(
                "Step2", 
                p -> { 
                    int[] param = p.castResult();
                    param[0]++;
                    p.setResult(param);
                },
                p -> { 
                    throw new IllegalStateException("Not Support Cancel");
                }
            )
            .call(
                "Step3", 
                p -> { 
                    int[] param = p.castResult();
                    param[0]++;
                    p.setResult(param);
                },
                p -> { 
                    throw new IllegalStateException("Not Support Cancel");
                }
            )
            .exec();
        assert value[0] == 3;
    }

    //#region TCC事务管理器

    private SimpleTCCManager buildMananger() {
        return SimpleTCCManager.builder()
                .setRepositoryGear(new IRepositoryGear() {

                    @Override
                    public boolean create(TCCTask taskModel) {
                        System.out.println("create TCCTaskModel");
                        return true;
                    }

                    @Override
                    public boolean update(TCCTask taskModel) {
                        System.out.println("update TCCTaskModel");
                        return true;
                    }

                    @Override
                    public TCCTask get(String tccTransactionId) {
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


        // other builder
        // return SimpleTCCManager.extendBuilder()
        //     .setTCCBeginBiz(new SimpleTCCBeginBiz())
        //     .setTCCUpdateBiz(new SimpleTCCUpdateBiz())
        //     .setTCCEndBiz(new SimpleTCCEndBiz())
        //     .setTCCConfirmRetryBiz(new SimpleTCCConfirmRetryBiz())
        //     .setTCCCancelRetryBiz(new SimpleTCCCancelRetryBiz())
        //     .build();
    }
    

    //#endregion

    //#region TCC补偿基础数据

    private SimpleTCCManager buildCompensationManager(TCCStatus failedStatus) {
        return SimpleTCCManager.builder()
                .setTCCTransactionId(UUID.randomUUID().toString())
                .setRepositoryGear(new IRepositoryGear() {

                    @Override
                    public boolean create(TCCTask taskModel) {
                        return true;
                    }

                    @Override
                    public boolean update(TCCTask taskModel) {
                        return true;
                    }

                    @Override
                    public TCCTask get(String tccTransactionId) {
                        return createCompensationTCCTask(tccTransactionId, failedStatus);
                    }
                    
                })
                .setSerializeGear(new ISerializeGear() {
                    @Override
                    public String serialize(Object obj) {
                        return obj != null ? obj.toString() : null;
                    }

                    @Override
                    public Object deserialize(String serializeStr) {
                        return serializeStr;
                    }
                    
                })
                .build();
    }

    private TCCTask createCompensationTCCTask(String transactionId, TCCStatus failedStatus) {
        TCCTask tccTask = new TCCTask();
        tccTask.setTransactionId(transactionId);
        tccTask.setPhase(
            failedStatus == TCCStatus.ConfirmFailed ? TCCPhase.CONFIRM : TCCPhase.CONFIRM);
        tccTask.setParamStr("the message content.");
        tccTask.setTccStatusCode(failedStatus.getValue());
        tccTask.setTaskStatus(TCCTaskStatus.WaitingForRetry.name());
        tccTask.setRetryTimes(0);

        List<TCCTaskNode> nodeList = new ArrayList<>();
        TCCTaskNode node1 = new TCCTaskNode();
        node1.setNodeId("1");
        node1.setNodeName("Node1");
        node1.setNodeParamStr(null);
        node1.setStatusCode(failedStatus.getValue());
        nodeList.add(node1);

        TCCTaskNode node2 = new TCCTaskNode();
        node2.setNodeId("2");
        node2.setNodeName("Node2");
        node2.setNodeParamStr(null);
        node2.setStatusCode(failedStatus.getValue());
        nodeList.add(node2);

        TCCTaskNode node3 = new TCCTaskNode();
        node3.setNodeId("3");
        node3.setNodeName("Node3");
        node3.setNodeParamStr(null);
        node3.setStatusCode(failedStatus.getValue());
        nodeList.add(node3);

        tccTask.setNodeList(nodeList);

        return tccTask;
    }

    //#endregion

}
