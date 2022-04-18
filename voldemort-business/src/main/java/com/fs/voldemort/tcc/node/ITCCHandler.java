package com.fs.voldemort.tcc.node;

/**
 * TCC事务节点的接口
 */
public interface ITCCHandler {
    
    /**
     * 检查预留资源，无状态 
     * @param parameter 参数
     * 【超时】：请抛出 TCCTimeoutException
     */
    void goTry(TCCNodeParameter parameter);

    /**
     * 提交操作，支持幂等，支持悬挂提交
     * @param parameter 参数
     * 【超时】：请抛出 TCCTimeoutException
     */
    void confirm(TCCNodeParameter parameter);

    /**
     * 回滚操作，支持幂等，支持悬挂提交
     * @param parameter 参数
     * 【超时】：请抛出 TCCTimeoutException
     */
    void cancel(TCCNodeParameter parameter);

}
