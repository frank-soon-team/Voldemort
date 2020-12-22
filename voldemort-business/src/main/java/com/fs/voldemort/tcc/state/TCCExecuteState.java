package com.fs.voldemort.tcc.state;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.node.TCCNode;

public class TCCExecuteState implements ITCCState {

    private final String id;
    private boolean rollback = false;
    private boolean end = false;
    private int status = TCCStatus.Initail.getStatus();
    // 待提交、待回滚列表
    private final List<TCCNode> triedTCCNodeList;
    // 异常列表
    private final List<ExecuteCallerNodeException> errorCollection;

    public TCCExecuteState(int size) {
        id = UUID.randomUUID().toString();
        triedTCCNodeList = new ArrayList<>(size);
        errorCollection = new ArrayList<>(size);
    }

    @Override
    public String identify() {
        return id;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    @Override
    public boolean isEnd() {
        return end;
    }

    public void setRollback(boolean rollback) {
        this.rollback = rollback;
    }

    @Override
    public boolean isRollback() {
        return rollback;
    }

    public void setStatus(TCCStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("the parameter status is required.");
        }
        this.status = status.getStatus();
    }

    @Override
    public int getStatus() {
        return status;
    }
    

    @Override
    public void collectExceptional(ExecuteCallerNodeException e) {
        errorCollection.add(e);

    }

    @Override
    public ExecuteCallerNodeException getCallerNodeException() {
        return errorCollection.isEmpty() ? null : errorCollection.get(0);
    }

    @Override
    public List<ExecuteCallerNodeException> getExceptionalCollection() {
        return errorCollection;
    }

    @Override
    public void addTriedNode(TCCNode node) {
        triedTCCNodeList.add(node);

    }

    @Override
    public List<TCCNode> getTriedNodeList() {
        return triedTCCNodeList;
    }
}
