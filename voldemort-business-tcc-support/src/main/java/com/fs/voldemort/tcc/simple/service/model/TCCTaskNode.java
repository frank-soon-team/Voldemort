package com.fs.voldemort.tcc.simple.service.model;

import java.io.Serializable;

public class TCCTaskNode implements Serializable {

    /** TCC 节点ID */
    private String nodeId;

    /** TCC 节点名称 */
    private String nodeName;

    /** TCC 节点参数 */
    private String nodeParamStr;

    /** TCC 节点状态码 */
    private int statusCode;

    /** 节点错误信息 */
    private String errorMessage;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

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
