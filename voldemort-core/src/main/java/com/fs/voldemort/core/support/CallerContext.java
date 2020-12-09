package com.fs.voldemort.core.support;

public class CallerContext {
    
    private ValueBag valueBag = new ValueBag();
    private CallerContext parentContext;

    public CallerContext() {

    }

    public CallerContext(CallerContext parentContext) {
        this.parentContext = parentContext;
    }

    /**
     * 获取值
     * 优先从当前上下文中获取，如果没有则尝试去parentContext中获取
     * @param key
     */
    public Object get(String key) {
        Object value = valueBag.get(key);
        if(value == null && parentContext != null) {
            value = parentContext.get(key);
        }
        return value;
    }

    /**
     * 将值放入Context中，如果key已经存在，则覆盖
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        valueBag.set(key, value);
    }

    /**
     * 更新Context，如果key不存在则不会放入value
     * @param key
     */
    public boolean update(String key, Object value) {
        if(valueBag.contains(key)) {
            valueBag.set(key, value);
            return true;
        }

        if(parentContext != null) {
            return parentContext.update(key, value);
        }

        return false;
    }
    
}
