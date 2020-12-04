package com.fs.voldemort.core.support;

import com.fs.voldemort.core.exception.CallerException;
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
            Object r= container.get(key);
            if(r == null) {
                return null;
            }

            if(!(r instanceof BigDecimal)) {
                throw new CallerException(String.format("Param.Context get error, please check the type of target result![Key:%s]",key));
            }
            return (BigDecimal)r;
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
