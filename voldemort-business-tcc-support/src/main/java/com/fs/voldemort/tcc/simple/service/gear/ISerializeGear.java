package com.fs.voldemort.tcc.simple.service.gear;

public interface ISerializeGear {

    String serialize(Object obj);

    <T> T deserialize(String serializeStr);
    
}
