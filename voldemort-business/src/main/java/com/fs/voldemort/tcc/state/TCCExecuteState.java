package com.fs.voldemort.tcc.state;

import java.util.ArrayList;
import java.util.List;

import com.fs.voldemort.tcc.node.TCCNode;

public class TCCExecuteState implements ITCCState {
    private boolean rollback = false;
    private boolean end = false;
    // 待提交、待回滚列表
    private final List<TCCNode> triedTCCNodeList;
    // 异常列表
    private final List<RuntimeException> errorCollection;

    public TCCExecuteState(int size) {
        triedTCCNodeList = new ArrayList<>(size);
        errorCollection = new ArrayList<>(size);
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setRollback(boolean rollback) {
        this.rollback = rollback;
    }

    @Override
    public boolean isRollback() {
        return rollback;
    }

    @Override
    public boolean isEnd() {
        return end;
    }

    @Override
    public void collectExceptional(RuntimeException e) {
        errorCollection.add(e);

    }

    @Override
    public List<RuntimeException> getExceptionalCollection() {
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
