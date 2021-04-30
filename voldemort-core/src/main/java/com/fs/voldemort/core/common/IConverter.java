package com.fs.voldemort.core.common;

import java.math.BigDecimal;
import java.util.Date;

public interface IConverter<K> {

    Byte getByteValue(K key);

    Character getCharValue(K key);
    
    String getStringValue(K key);

    Boolean getBooleanValue(K key);

    Short getShortValue(K key);

    Integer getIntegerValue(K key);

    Long getLongValue(K key);

    Float getFloatValue(K key);

    Double getDoubleValue(K key);

    BigDecimal getBigDecimalValue(K key);

    Date getDateValue(K key);

    Date getDateValue(K key, String dateFormatter);

    <T> T getValue(K key);

}
