package com.fs.voldemort.parallel;

import java.util.concurrent.CountDownLatch;

import com.fs.voldemort.core.exception.CrucioException;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerNode;
import com.fs.voldemort.core.support.CallerParameter;

public class ParallelTaskNode extends CallerNode implements Runnable {

    private CountDownLatch currentCountDownLatch;
    private CallerParameter currentCallerParameter;
    private Action1<ParallelTaskResult.ResultModel> currentResultSetter;

    public ParallelTaskNode(Func1<CallerParameter, Object> actionFunc) {
        super(actionFunc);
    }

    @Override
    public void run() {
        ParallelTaskResult.ResultModel result = new ParallelTaskResult.ResultModel();
        try {
            Object value = doAction(currentCallerParameter);
            result.setValue(value);
            if(currentCountDownLatch != null) {
                currentCountDownLatch.countDown();
            }
        } catch(Exception e) {
            result.setValue(new CrucioException("execute parallelTask error.", e));
        } finally {
            currentResultSetter.apply(result);
            reset();
        }
    }

    public CountDownLatch getCurrentCountDownLatch() {
        return currentCountDownLatch;
    }

    public void setCurrentCountDownLatch(CountDownLatch currentCountDownLatch) {
        this.currentCountDownLatch = currentCountDownLatch;
    }

    public CallerParameter getCurrentCallerParameter() {
        return currentCallerParameter;
    }

    public void setCurrentCallerParameter(CallerParameter currentCallerParameter) {
        this.currentCallerParameter = currentCallerParameter;
    }

    public Action1<ParallelTaskResult.ResultModel> getCurrentResultSetter() {
        return currentResultSetter;
    }

    public void setCurrentResultSetter(Action1<ParallelTaskResult.ResultModel> currentResultSetter) {
        this.currentResultSetter = currentResultSetter;
    }

    protected void reset() {
        this.currentCountDownLatch = null;
        this.currentCallerParameter = null;
        this.currentResultSetter = null;
    }
    
}
