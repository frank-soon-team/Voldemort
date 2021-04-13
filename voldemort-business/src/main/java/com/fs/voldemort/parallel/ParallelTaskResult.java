package com.fs.voldemort.parallel;

import com.fs.voldemort.action.Action1;
import com.fs.voldemort.core.exception.CrucioException;

public class ParallelTaskResult {

    private int index = 0;
    private final ResultModel[] resultValues;

    public ParallelTaskResult(int capacity) {
        if(capacity < 0) {
            capacity = 0;
        }
        resultValues = new ResultModel[capacity];
    }

    public Object getResult() {
        return getValue(index++);
    }

    public Object getResult(int index) {
        return getValue(index);
    }

    public void forEach(Action1<Object> actionFn) {
        if(actionFn == null) {
            throw new IllegalArgumentException("the actionFn is required.");
        }

        for(int i = 0; i < resultValues.length; i++) {
            actionFn.apply(getValue(i));
        }
    }

    public Action1<ResultModel> getValueSetter(final int index) {
        return result -> {
            resultValues[index] = result;
        };
    }

    private Object getValue(int index) {
        if(index >= resultValues.length) {
            throw new IndexOutOfBoundsException(index);
        }

        ResultModel result = resultValues[index];

        Exception errorValue = result.getErrorValue();
        if(errorValue != null) {
            if(errorValue instanceof RuntimeException) {
                throw (RuntimeException) errorValue;
            } else {
                throw new CrucioException(errorValue.getMessage(), errorValue);
            }
        }

        return result.getValue();
    }

    public static class ResultModel {

        private Object value;
        private Exception errorValue;

        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            this.value = value;
        }

        public Exception getErrorValue() {
            return errorValue;
        }
        public void setErrorValue(Exception errorValue) {
            this.errorValue = errorValue;
        }

    }
    
}
