package com.fs.voldemort.tcc.node;

/**
 * TCC接入节点的模板
 */
public abstract class BaseTCCHandler implements ITCCHandler {

    private final String name;

    protected BaseTCCHandler(String name) {
        this.name = name;
    }

    /**
     * TCC处理器的名称，用于在容器中注册时作为key
     * @return
     */
    public String getName() {
        return name;
    }

    protected String getDefaultName() {
        return this.getClass().getName();
    }

    // public String serialize() {
    //     // 序列化
    // }

    // public String deserialize() {
        
    // } 
    
}
