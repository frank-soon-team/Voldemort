package com.fs.voldemort.tcc.simple.service.gear;

import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;

public interface IRepositoryGear {

    /**
     * 获取一个TCC 任务信息
     */
    TCCTaskModel get(String tccTransactionId);

    /**
     * 新建一个TCC 任务
     * @param taskModel TCC任务信息
     * @return 创建成功返回 true，反之返回 false
     */
    boolean create(TCCTaskModel taskModel);

    /**
     * 更新TCC任务状态，仅需更新状态信息和errorMessage
     * @param taskModel 状态更新后的任务信息
     * @return 更新成功返回 true，反之返回 false
     */
    boolean update(TCCTaskModel taskModel);
    
}
