package com.fs.voldemort.tcc.simple.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TCCTaskModel implements Serializable {

    private String transactionId;

    private int statusCode;
    
    private String paramStr;

    private String statusDescription;

    private int retryTimes;

    private String traceId;

    private String bizCode;

    private List<TCCTaskNode> nodeList;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
