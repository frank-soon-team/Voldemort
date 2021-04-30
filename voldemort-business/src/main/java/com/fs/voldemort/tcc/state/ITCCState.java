package com.fs.voldemort.tcc.state;

import java.util.List;

import com.fs.voldemort.tcc.exception.ExecuteCallerNodeException;
import com.fs.voldemort.tcc.exception.TCCStateException;
import com.fs.voldemort.tcc.node.TCCNode;

public interface ITCCState {

    String identify();

    void collectExceptional(ExecuteCallerNodeException e);

    void setStateException(TCCStateException e);

    TCCStateException getStateException();

    ExecuteCallerNodeException getCallerNodeException();

    List<ExecuteCallerNodeException> getExceptionalCollection();

    void addTriedNode(TCCNode node);

    List<TCCNode> getTriedNodeList();

    boolean isConfirm();

    boolean isSuccess();

    boolean isEnd();

    int getStatus();
    
}
