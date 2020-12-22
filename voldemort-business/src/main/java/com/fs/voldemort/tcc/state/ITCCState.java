package com.fs.voldemort.tcc.state;

import java.util.List;

import com.fs.voldemort.tcc.node.TCCNode;

public interface ITCCState {

    void collectExceptional(RuntimeException e);

    List<RuntimeException> getExceptionalCollection();

    void addTriedNode(TCCNode node);

    List<TCCNode> getTriedNodeList();

    boolean isEnd();

    boolean isRollback();
    
}
