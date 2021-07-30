package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerParameter;

/**
 * TCC事务节点的接口
 */
public interface ITCCHandler {
    
    // 检查预留资源，无状态
    Object goTry(CallerParameter parameter);

    // 提交操作，支持幂等，支持悬挂提交
    void confirm(CallerParameter parameter);

    // 回滚操作，支持幂等，支持悬挂提交
    void cancel(CallerParameter parameter);

}
