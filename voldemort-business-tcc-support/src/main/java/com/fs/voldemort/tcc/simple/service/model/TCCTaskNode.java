package com.fs.voldemort.tcc.simple.service.model;

public class TCCTaskNode {

    private String nodeName;

    private String nodeParamStr;

    private int statusCode;

    private String errorMessage;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeParamStr() {
        return nodeParamStr;
    }

    public void setNodeParamStr(String nodeParamStr) {
        this.nodeParamStr = nodeParamStr;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
    
}
