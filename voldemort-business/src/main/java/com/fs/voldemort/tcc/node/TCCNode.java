package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.state.TCCStatus;

public class TCCNode extends CallerNode {

    // TCC 节点名称，在一个Caller中，name不能重复
    private final String name;
    // TCC 任务执行器
    private final ITCCHandler tccHandler;
    // 参数
    private TCCNodeParameter nodeParameter;
    // 状态
    private TCCStatus status;
    // 错误信息
    private ExecuteCallerNodeException error;
    // 节点ID
    private String nodeId;

    public TCCNode(String name, ITCCHandler tccHandler) {
        this(name, tccHandler, TCCStatus.TryPending);
    }

    public TCCNode(String name, ITCCHandler tccHandler, TCCStatus status) {
        super(p -> {
            tccHandler.goTry((TCCNodeParameter) p);
            return p.result;
        });
        this.status = status;
        this.tccHandler = tccHandler;
        this.name = name;
    }

    public void doConfirm() {
        if(nodeParameter == null) {
            throw new IllegalStateException("the nodeParameter is null.");
        }
        tccHandler.confirm(nodeParameter);
    }

    public void doCancel() {
        if(nodeParameter == null) {
            throw new IllegalStateException("the nodeParameter is null.");
        }
        tccHandler.cancel(nodeParameter);
    }
    
    public void setNodeParameter(TCCNodeParameter parameter) {
        this.nodeParameter = parameter;
    }
    
    public TCCNodeParameter getNodeParameter() {
        return this.nodeParameter;
    }

    public ITCCHandler getTCCHandler() {
        return tccHandler;
    }

    public TCCStatus getStatus() {
        return status;
    }

    public void setStatus(TCCStatus status) {
        this.status = status;
    }

    public ExecuteCallerNodeException getError() {
        return error;
    }

    public void setError(ExecuteCallerNodeException error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
}
