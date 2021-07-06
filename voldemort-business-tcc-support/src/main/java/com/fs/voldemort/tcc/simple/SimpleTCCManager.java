package com.fs.voldemort.tcc.simple;

import com.fs.voldemort.tcc.TCCManager;
import com.fs.voldemort.tcc.simple.adapter.SimpleCancelCompensateAdapter;
import com.fs.voldemort.tcc.simple.adapter.SimpleStateManagerAdapter;
import com.fs.voldemort.tcc.simple.service.biz.SImpleTCCCancelRetryBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCBeginBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCConfirmRetryBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCEndBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCUpdateBiz;
import com.fs.voldemort.tcc.simple.adapter.SimpleConfirmCompensateAdapter;

public class SimpleTCCManager extends TCCManager {

    private SimpleTCCManager() {
    }


    public static SimpleTCCManagerBuilder builder() {
        return new SimpleTCCManagerBuilder();
    }
    

    public static class SimpleTCCManagerBuilder {

        private SimpleTCCManagerBuilder() {

        }

        private SimpleTCCBeginBiz tccBeginBiz;

        private SimpleTCCUpdateBiz tccUpdateBiz;

        private SimpleTCCEndBiz tccEndBiz;

        private SimpleTCCConfirmRetryBiz tccConfirmRetryBiz;

        private SImpleTCCCancelRetryBiz tccCancelRetryBiz;

        public SimpleTCCManagerBuilder setTCCBeginBiz(SimpleTCCBeginBiz beginBiz) {
            this.tccBeginBiz = beginBiz;
            return this;
        }

        public SimpleTCCManagerBuilder setTCCUpdateBiz(SimpleTCCUpdateBiz updateBiz) {
            this.tccUpdateBiz = updateBiz;
            return this;
        }

        public SimpleTCCManagerBuilder setTCCEndBiz(SimpleTCCEndBiz endBiz) {
            this.tccEndBiz = endBiz;
            return this;
        }

        public SimpleTCCManagerBuilder setTCCConfirmRetryBiz(SimpleTCCConfirmRetryBiz confirmRetryBiz) {
            this.tccConfirmRetryBiz = confirmRetryBiz;
            return this;
        }

        public SimpleTCCManagerBuilder setTCCCancelRetryBiz(SImpleTCCCancelRetryBiz cancelRetryBiz) {
            this.tccCancelRetryBiz = cancelRetryBiz;
            return this;
        }


        public SimpleTCCManager build() {
            SimpleTCCManager simpleTCCManager = new SimpleTCCManager();
            simpleTCCManager.setStateManager(
                new SimpleStateManagerAdapter(tccBeginBiz, tccUpdateBiz, tccEndBiz)
            );
            simpleTCCManager.setConfirmCompensateStrategy(
                new SimpleConfirmCompensateAdapter(tccConfirmRetryBiz)
            );
            simpleTCCManager.setCancelCompensateStrategy(
                new SimpleCancelCompensateAdapter(tccCancelRetryBiz)
            );
            return simpleTCCManager;
        }

    }
}
