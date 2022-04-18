package com.fs.voldemort.core.support;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fs.voldemort.core.common.IConverter;

public class ValueBag implements IBag<String, Object>, IConverter<String> {

    private Map<String, Object> contextStore = new LinkedHashMap<>(16);

    @Override
    public Object get(String key) {
        if(key == null || key.length() == 0) {
            return null;
        }
        return contextStore.get(key);
    }

    @Override
    public void set(String key, Object value) {
        if(key == null || key.length() == 0) {
            throw new IllegalArgumentException("the parameter [key] is required.");
        }
        contextStore.put(key, value);
    }

    @Override
    public boolean remove(String key) {
        if(key == null || key.length() == 0) {
            return false;
        }
        return contextStore.remove(key) != null;
    }

    @Override
    public boolean contains(String key) {
        return contextStore.containsKey(key);
    }

    @Override
    public Set<String> getKeys() {
        return new HashSet<>(contextStore.keySet());
    }

    @Override
    public boolean isEmpty() {
        return contextStore.isEmpty();
    }

    @Override
    public Byte getByteValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return Byte.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public Character getCharValue(String key) {
        return getValue(key);
    }

    @Override
    public String getStringValue(String key) {
        return getValue(key);
    }

    @Override
    public Boolean getBooleanValue(String key) {
        Object value = get(key);
        if(value instanceof Boolean) {
            return Boolean.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public Short getShortValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return Short.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public Integer getIntegerValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return Integer.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public Long getLongValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return Long.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public Float getFloatValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return Float.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public Double getDoubleValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return Double.valueOf((String) value);
        }
        return getValue(key);
    }

    @Override
    public BigDecimal getBigDecimalValue(String key) {
        Object value = get(key);
        if(value instanceof String) {
            return new BigDecimal((String) value);
        }
        return getValue(key);
    }

    @Override
    public Date getDateValue(String key) {
        return getValue(key);
    }

    @Override
    public Date getDateValue(String key, String dateFormatter) {
        if(dateFormatter == null || dateFormatter.length() == 0) {
            throw new IllegalArgumentException("the parameter [dateFormatter] is required.");
        }
        Object value = get(key);
        if(value instanceof String) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatter);
            String dateValue = (String) value;
            try {
                return format.parse(dateValue);
            } catch (ParseException e) {
                throw new DateTimeParseException("parse value of ValueBag ouccr error.", dateValue, e.getErrorOffset(), e);
            }
        }
        return getValue(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String key) {
        return (T) get(key);
    }
    
}
