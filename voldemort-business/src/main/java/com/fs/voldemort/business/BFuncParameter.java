package com.fs.voldemort.business;

import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerContext;
import com.fs.voldemort.core.support.CallerParameter;

public class BFuncParameter extends CallerParameter {

    // 节点参数，键值对结构，不同于Context上下文，参数只作用于当前节点
    private Func1<String, NodeParam> nodeParamGetFunc;

    public BFuncParameter(CallerParameter parameter) {
        super(parameter);
    }

    public BFuncParameter(Object result, CallerContext context) {
        super(result, context);
    }

    public Object getParameter(String name) {
        if(nodeParamGetFunc != null) {
            NodeParam param = nodeParamGetFunc.call(name);
            return param != null ? param.getValue() : null;
        }
        return null;
    }

    void setNodeParamGetFunc(Func1<String, NodeParam> nodeParamGetFunc) {
        this.nodeParamGetFunc = nodeParamGetFunc;
    }
    
}
