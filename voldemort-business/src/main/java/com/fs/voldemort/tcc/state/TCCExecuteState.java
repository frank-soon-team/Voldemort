package com.fs.voldemort.tcc.state;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.exception.TCCStateException;
import com.fs.voldemort.tcc.node.TCCNode;

public class TCCExecuteState implements ITCCState {

    // TCC事务ID
    private final String tccTransactionId;
    // 当前执行状态
    private TCCStatus status = TCCStatus.Initail;
    // 待提交、待回滚列表
    private final List<TCCNode> tccNodeList;
    // 异常列表
    private final List<ExecuteCallerNodeException> errorCollection;
    // TCC状态异常
    private TCCStateException stateException;
    // TCC参数
    private Object param;

    public TCCExecuteState(int size) {
        this(UUID.randomUUID().toString(), size);
    }

    public TCCExecuteState(Func0<String> tccTransactionIdGenrator, int size) {
        this(tccTransactionIdGenrator == null ? null : tccTransactionIdGenrator.call(), size);
    }

    public TCCExecuteState(String tccTransactionId, int size) {
        this.tccTransactionId = tccTransactionId;
        if(this.tccTransactionId == null || this.tccTransactionId.length() == 0) {
            throw new IllegalArgumentException("the parameter tccTransactionId is required.");
        }
        tccNodeList = new ArrayList<>(size);
        errorCollection = new ArrayList<>(size);
    }

    @Override
    public String identify() {
        return this.tccTransactionId;
    }

    @Override
    public boolean isConfirm() {
        return status == TCCStatus.TrySuccess;
    }

    @Override
    public boolean isSuccess() {
        return status == TCCStatus.ConfirmSuccess;
    }

    @Override
    public boolean isEnd() {
        int statusValue = status.getValue();
        return statusValue >= 20 && statusValue < 40;
    }

    public void setStatus(TCCStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("the parameter status is required.");
        }
        this.status = status;
    }

    @Override
    public TCCStatus getStatus() {
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
    public void addTCCNode(TCCNode node) {
        tccNodeList.add(node);

    }

    @Override
    public List<TCCNode> getTCCNodeList() {
        return tccNodeList.stream().collect(Collectors.toList());
    }

    @Override
    public List<TCCNode> getTriedNodeList() {
        return tccNodeList.stream()
                    .filter(n -> n.getStatus() != TCCStatus.Initail)
                    .collect(Collectors.toList());
    }

    @Override
    public List<TCCNode> getRollbackFailedList() {
        return tccNodeList.stream()
                    .filter(n -> n.getStatus() == TCCStatus.CancelFailed || n.getStatus() == TCCStatus.CancelTimeout)
                    .collect(Collectors.toList());
    }

    @Override
    public List<TCCNode> getConfirmFailedList() {
        return tccNodeList.stream()
                    .filter(n -> n.getStatus() == TCCStatus.ConfirmFailed || n.getStatus() == TCCStatus.ConfirmTimeout)
                    .collect(Collectors.toList());
    }

    @Override
    public void setStateException(TCCStateException e) {
        stateException = e;
    }

    @Override
    public TCCStateException getStateException() {
        return stateException;
    }

    @Override
    public void setParam(Object param) {
        this.param = param;
    }

    @Override
    public Object getParam() {
        return param;
    }
}
