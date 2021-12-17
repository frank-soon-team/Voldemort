package com.fs.voldemort.business.paramfinder;

public class SimpleFindResult implements ParamNameTypeFindResult{

    private String name;

    private Class<?> clazz;

    public SimpleFindResult() {
    }

    public SimpleFindResult(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getParamName() {
        return this.name;
    }

    @Override
    public Class<?> getParamClazz() {
        return this.clazz;
    }
}
