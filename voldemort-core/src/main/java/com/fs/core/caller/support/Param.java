package com.fs.core.caller.support;

import com.fs.core.caller.exception.CallerException;
import com.sun.javafx.binding.StringFormatter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author frank
 */
public class Param<T> {

    public final T result;

    public final Context context;

    public Param(T result, Map<String,Object> contextMap) {
        this.result = result;
        this.context = new Context(contextMap);
    }

    public Param(Map<String,Object> contextMap) {
        this.result = null;
        this.context = new Context(contextMap);
    }

    public class Context{

        private final Map<String,Object> container;

        public Context(@NonNull final Map<String,Object> context) {
            this.container = context;
        }

        public BigDecimal getBigDecimal(@NonNull final String key) {
            Object result = container.get(key);
            if(result == null) {
                return null;
            }

            if(!(result instanceof BigDecimal)) {
                throw new CallerException(StringFormatter
                        .format("Param.Context get error, please check the type of target result![Key:%S]",key)
                        .getValue());
            }
            return (BigDecimal)result;
        }

        @SuppressWarnings("unchecked")
        public <R> R get(@NonNull final String key) {
            return (R)container.get(key);
        }

        public void set(@NonNull final String key, @NonNull final T value){
            container.put(key,value);
        }

        public boolean isEmpty() {
            return container.isEmpty();
        }

    }

}
