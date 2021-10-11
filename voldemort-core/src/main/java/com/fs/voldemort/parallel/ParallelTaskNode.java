package com.fs.voldemort.parallel;

import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;

public class ParallelTaskNode extends CallerNode {

    public ParallelTaskNode(Func1<CallerParameter, Object> actionFunc) {
        super(actionFunc);
    }
    
}
