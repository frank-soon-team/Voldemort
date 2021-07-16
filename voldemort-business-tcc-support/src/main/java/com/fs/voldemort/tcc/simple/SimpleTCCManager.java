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

    public static SimpleTCCManagerExtendBuilder extendBuilder() {
        return new SimpleTCCManagerExtendBuilder();
    }

    public static class SimpleTCCManagerBuilder {

    }
    

    public static class SimpleTCCManagerExtendBuilder extends SimpleTCCManagerBuilder {

        private SimpleTCCManagerExtendBuilder() {

        }

        private SimpleTCCBeginBiz tccBeginBiz;

        private SimpleTCCUpdateBiz tccUpdateBiz;

        private SimpleTCCEndBiz tccEndBiz;

        private SimpleTCCConfirmRetryBiz tccConfirmRetryBiz;

        private SImpleTCCCancelRetryBiz tccCancelRetryBiz;

        public SimpleTCCManagerExtendBuilder setTCCBeginBiz(SimpleTCCBeginBiz beginBiz) {
            this.tccBeginBiz = beginBiz;
            return this;
        }

        public SimpleTCCManagerExtendBuilder setTCCUpdateBiz(SimpleTCCUpdateBiz updateBiz) {
            this.tccUpdateBiz = updateBiz;
            return this;
        }

        public SimpleTCCManagerExtendBuilder setTCCEndBiz(SimpleTCCEndBiz endBiz) {
            this.tccEndBiz = endBiz;
            return this;
        }

        public SimpleTCCManagerExtendBuilder setTCCConfirmRetryBiz(SimpleTCCConfirmRetryBiz confirmRetryBiz) {
            this.tccConfirmRetryBiz = confirmRetryBiz;
            return this;
        }

        public SimpleTCCManagerExtendBuilder setTCCCancelRetryBiz(SImpleTCCCancelRetryBiz cancelRetryBiz) {
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
