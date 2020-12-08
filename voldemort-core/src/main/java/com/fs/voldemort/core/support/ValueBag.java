package com.fs.voldemort.core.support;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fs.voldemort.core.common.IConverter;

public class ValueBag implements IBag<String, Object>, IConverter<String> {

    private Map<String, Object> contextStore = new LinkedHashMap<>(16);

    @Override
    public Object get(String key) {
        return contextStore.get(key);
    }

    @Override
    public void set(String key, Object value) {
        contextStore.put(key, value);
    }

    @Override
    public Byte getByteValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Character getCharValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStringValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean getBooleanValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Short getShortValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getIntegerValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getLongValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Float getFloatValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Double getDoubleValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getBigDecimalValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getDateValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
