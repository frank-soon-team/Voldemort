package com.fs.voldemort.tcc.simple.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TCCTaskModel implements Serializable {

    /** TCC 任务唯一标识 */
    private String transactionId;

    /** TCC 阶段标识 */
    private String stage;

    /** TCC 任务当前状态 */
    private int statusCode;
    
    /** TCC 执行参数 */
    private String paramStr;

    /** TCC 任务状态描述 */
    private String statusDescription;

    /** 重试次数 */
    private int retryTimes = 0;

    /** 跟踪ID */
    private String traceId;

    /** 业务编号，用于标识业务 */
    private String bizCode;

    private List<TCCTaskNode> nodeList;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getParamStr() {
        return paramStr;
    }

    public void setParamStr(String paramStr) {
        this.paramStr = paramStr;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public List<TCCTaskNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<TCCTaskNode> nodeList) {
        this.nodeList = nodeList;
    }

    public void addNode(TCCTaskNode node) {
        if(node == null) {
            throw new IllegalArgumentException("the parameter node is required.");
        }
        if(this.nodeList == null) {
            this.nodeList = new ArrayList<>();
        }
        this.nodeList.add(node);
    }

    
}
