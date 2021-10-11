package com.fs.voldemort.business;

import java.util.HashMap;
import java.util.Map;

import com.fs.voldemort.core.support.CallerNode;

public interface ICallWithParameter<T> {

    T call(Class<?> funcClazz, NodeParam... params);

    static void setParameterBeforeAction(CallerNode node, NodeParam... params) {
        if(node == null || params == null) {
            return;
        }

        final Map<String, NodeParam> map = new HashMap<>();
        for(NodeParam param : params) {
            if(param != null) {
                map.put(param.getName(), param);
            }
        }

        if(!map.isEmpty()) {
            node.setBeforeAction(p -> ((BFuncParameter)p).setNodeParamGetFunc(paramName -> map.get(paramName)));
        }
        
    }
    
}
