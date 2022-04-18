package com.fs.voldemort.tcc.node;

import com.fs.voldemort.tcc.state.TCCStatus;

public class TCCStateNode extends TCCNode {

    public TCCStateNode(String name, TCCStatus status) {
        super(name, new TCCHandlerWrapper(), status);
    }

    public void setTCCHandler(ITCCHandler tccHandler) {
        ((TCCHandlerWrapper) getTCCHandler()).setTccHandler(tccHandler);
    }
    
    public static class TCCHandlerWrapper implements ITCCHandler {
        private ITCCHandler tccHandler;
    
        public TCCHandlerWrapper() {
        }
    
        public TCCHandlerWrapper(ITCCHandler tccHandler) {
            this.tccHandler = tccHandler;
        }
    
        public ITCCHandler getTccHandler() {
            return tccHandler;
        }
    
        public void setTccHandler(ITCCHandler handler) {
            this.tccHandler = handler;
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
}
