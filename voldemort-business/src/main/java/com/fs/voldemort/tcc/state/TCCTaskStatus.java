package com.fs.voldemort.tcc.state;

public enum TCCTaskStatus {

    /** 初始状态 */
    Start,
    /** 任务完成 */
    Done,
    /** 未完成 */
    Incomplete,
    /** 任务等待重试 */
    WaitingForRetry,
    ;

    public String getValue() {
        return name();
    }
}
