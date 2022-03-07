package com.fs.voldemort.tcc.simple.service.model;

import java.util.List;

import com.fs.voldemort.tcc.node.TCCNode;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.state.ITCCState;

public interface Transfer {

    public static TCCTaskModel toTCCModel(ITCCState tccState, ISerializeGear serializeGear) {
        TCCTaskModel tccTaskModel = new TCCTaskModel();
        tccTaskModel.setTransactionId(tccState.identify());
        tccTaskModel.setStage(tccState.getStatus().getStage());
        tccTaskModel.setParamStr(serializeGear.serialize(tccState.getParam()));
        tccTaskModel.setStatusCode(tccState.getStatus().getValue());
        tccTaskModel.setStatusDescription(tccState.getStatus().name());

        List<TCCNode> nodeList = tccState.getTCCNodeList();
        if(nodeList != null) {
            for(TCCNode node : nodeList) {
                TCCTaskNode taskNode = new TCCTaskNode();
                taskNode.setNodeName(node.getName());
                taskNode.setNodeParamStr(serializeGear.serialize(node.getNodeParameter().result));
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
        return null;
    }
    
}
