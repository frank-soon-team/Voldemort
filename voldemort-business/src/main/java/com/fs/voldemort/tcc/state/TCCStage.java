package com.fs.voldemort.tcc.state;

public interface TCCStage {

    /** 初始 */
    public final static String INITIAL = "INITIAL";

    /** 一阶段 Try */
    public final static String TRY = "TRY";

    /** 二阶段 确认 */
    public final static String CONFIRM = "CONFIRM";

    /** 二阶段 取消 */
    public final static String CANCEL = "CANCEL";
    
}
