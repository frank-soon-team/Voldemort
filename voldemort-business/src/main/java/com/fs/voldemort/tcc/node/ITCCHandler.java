package com.fs.voldemort.tcc.node;

import com.fs.voldemort.core.support.CallerParameter;
import com.fs.voldemort.tcc.state.ITCCState;

/**
 * TCC事务节点的接口
 */
public interface ITCCHandler {
    
    /**
     * 检查预留资源，无状态
     * @return
     */
    Object goTry(CallerParameter parameter);

    /**
     * 提交操作，支持幂等，支持悬挂提交
     * @return
     */
    void confirm(ITCCState tccState);

    /**
     * 回滚操作，支持幂等，支持悬挂提交
     */
    void cancel(ITCCState tccState);

}
