package com.fs.voldemort.tcc.simple.service.gear;

public interface ISerializeGear {

    String serialize(Object obj);

    Object deserialize(String serializeStr);
    
}
