package com.fs.voldemort.tcc.simple.service.model;

import java.util.List;

import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.node.TCCStateNode;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;
import com.fs.voldemort.tcc.state.TCCStatus;
import com.fs.voldemort.tcc.state.TCCTaskStatus;

public interface Transfer {

    public static TCCTaskModel toTCCModel(ITCCState tccState, ISerializeGear serializeGear) {
        TCCTaskModel tccTaskModel = new TCCTaskModel();
        tccTaskModel.setTransactionId(tccState.getTCCTransactionId());
        tccTaskModel.setStage(tccState.getStatus().getStage());
        tccTaskModel.setParamStr(serializeGear.serialize(tccState.getParam()));
        tccTaskModel.setTaskStatus(tccState.getTaskStatus().getValue());
        tccTaskModel.setTccStatusCode(tccState.getStatus().getValue());
        tccTaskModel.setStatusDescription(tccState.getStatus().name());

        ExecuteCallerNodeException tryException = tccState.getCallerNodeException();
        if(tryException != null) {
            tccTaskModel.setTryErrorMessage(tryException.getMessage());
        }

        List<TCCNode> nodeList = tccState.getTCCNodeList();
        if(nodeList != null) {
            for(TCCNode node : nodeList) {
                TCCTaskNode taskNode = new TCCTaskNode();
                taskNode.setNodeName(node.getName());
                if(node.getNodeParameter() != null) {
                    /* 
                        NodeParameter.result 可能是其他普通CallerNode计算的结果，
                        这些CallerNode是在 Try 阶段执行的，当需要补偿时，会直接补偿 Confirm 阶段或是 Cancel 阶段，
                        所以，需要将当前的 result 序列化保存，以便补偿时还原。
                        多数情况下，NodeParameter.result 为 null。
                    */
                    taskNode.setNodeParamStr(serializeGear.serialize(node.getNodeParameter().result));
                }
                taskNode.setStatusCode(node.getStatus().getValue());
                if(node.getError() != null) {
                    taskNode.setErrorMessage(node.getError().getMessage());
                }

                tccTaskModel.addNode(taskNode);
            }
        }

        return tccTaskModel;
    }

    public static ITCCState toTCCState(TCCTaskModel model, ISerializeGear serializeGear) {
        List<TCCTaskNode> taskNodeList = model.getNodeList();
        if(taskNodeList == null || taskNodeList.isEmpty()) {
            throw new IllegalArgumentException("the [taskNodeList] of parameter model is null or empty.");
        }

        TCCExecuteState tccState = new TCCExecuteState(model.getTransactionId(), taskNodeList.size());
        tccState.setTaskStatus(TCCTaskStatus.valueOf(model.getTaskStatus()));
        tccState.setStatus(TCCStatus.valueOf(model.getTccStatusCode()));
        tccState.setParam(serializeGear.deserialize(model.getParamStr()));

        CallerContext callerContext = new CallerContext();
        for(TCCTaskNode taskNode : taskNodeList) {
            TCCStateNode tccStateNode = new TCCStateNode(
                taskNode.getNodeName(), TCCStatus.valueOf(taskNode.getStatusCode()));

            TCCNodeParameter tccNodeParameter = new TCCNodeParameter(
                serializeGear.deserialize(taskNode.getNodeParamStr()), callerContext);
            tccStateNode.setNodeParameter(tccNodeParameter);

            tccState.addTCCNode(tccStateNode);
        }

        tccState.setStateType(TCCExecuteState.TCC_STATE_COMPENSATION);

        return tccState;
    }
    
}
