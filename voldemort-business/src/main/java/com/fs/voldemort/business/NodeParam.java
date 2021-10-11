package com.fs.voldemort.business;

public class NodeParam {

    private final String name;
    private final Object value;

    public NodeParam(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
    
}
