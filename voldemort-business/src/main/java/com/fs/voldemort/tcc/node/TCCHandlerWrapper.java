package com.fs.voldemort.tcc.node;

public class TCCHandlerWrapper implements ITCCHandler {
    private String name;
    private ITCCHandler tccHandler;

    public TCCHandlerWrapper(String name) {
        this(name, null);
    }

    public TCCHandlerWrapper(String name, ITCCHandler tccHandler) {
        this.tccHandler = tccHandler;
        this.name = name;
    }

    public ITCCHandler getTccHandler() {
        return tccHandler;
    }

    public void setTccHandler(ITCCHandler handler) {
        this.tccHandler = handler;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void goTry(TCCNodeParameter parameter) {
        ensureTCCHandler().goTry(parameter);
    }

    @Override
    public void confirm(TCCNodeParameter parameter) {
        ensureTCCHandler().confirm(parameter);
    }

    @Override
    public void cancel(TCCNodeParameter parameter) {
        ensureTCCHandler().cancel(parameter);
    }

    private ITCCHandler ensureTCCHandler() {
        if(tccHandler == null) {
            throw new NullPointerException("TCCHandlerWrapper [tccHandler] is null.");
        }
        return tccHandler;
    }
}
