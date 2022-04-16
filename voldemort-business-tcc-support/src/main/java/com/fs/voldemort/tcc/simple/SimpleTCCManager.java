package com.fs.voldemort.tcc.simple;

import com.fs.voldemort.core.functional.func.Func0;
import com.fs.voldemort.tcc.ITCCManagerAdapter;
import com.fs.voldemort.tcc.TCCManager;
import com.fs.voldemort.tcc.simple.adapter.SimpleStateManagerAdapter;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCBeginBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCCompensateBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCEndBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCStateBiz;
import com.fs.voldemort.tcc.simple.service.biz.SimpleTCCUpdateBiz;
import com.fs.voldemort.tcc.simple.service.gear.IBusinessSupportGear;
import com.fs.voldemort.tcc.simple.service.gear.IRepositoryGear;
import com.fs.voldemort.tcc.simple.service.gear.ISerializeGear;
import com.fs.voldemort.tcc.simple.service.model.TCCTaskModel;
import com.fs.voldemort.tcc.simple.service.model.Transfer;
import com.fs.voldemort.tcc.state.IStateManager;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.strategy.ICompensateStrategy;
import com.fs.voldemort.tcc.simple.adapter.SimpleCompensateAdapter;

public class SimpleTCCManager extends TCCManager {

    private SimpleTCCManager(ITCCManagerAdapter adapter) {
        super(adapter);
    }


    public static SimpleTCCManagerBuilder builder() {
        return new SimpleTCCManagerBuilder();
    }

    public static SimpleTCCManagerExtendBuilder extendBuilder() {
        return new SimpleTCCManagerExtendBuilder();
    }

    public static class SimpleTCCManagerAdapter implements ITCCManagerAdapter {

        private Func0<ITCCState> tccStateGetter;
        private IStateManager stateManager;
        private ICompensateStrategy compensateStrategy;

        public void setStateManager(IStateManager stateManager) {
            this.stateManager = stateManager;
        }

        @Override
        public IStateManager getStateManager() {
            return this.stateManager;
        }

        public void setCompensateStrategy(ICompensateStrategy compensateStrategy) {
            this.compensateStrategy = compensateStrategy;
        }

        @Override
        public ICompensateStrategy getCompensateStrategy() {
            return this.compensateStrategy;
        }

        public void setTCCStateGetter(Func0<ITCCState> tccStateGetter) {
            this.tccStateGetter = tccStateGetter;
        }

        @Override
        public Func0<ITCCState> getTCCStateGetter() {
            return this.tccStateGetter;
        }

    }

    public static class SimpleTCCManagerBuilder {

        private SimpleTCCManagerBuilder() {

        }

        private String tccTransactionId;

        private IRepositoryGear repositoryGear;
        
        private ISerializeGear serializeGear;

        private IBusinessSupportGear businessSupportGear;

        public SimpleTCCManagerBuilder setTCCTransactionId(String tccTransactionId) {
            this.tccTransactionId = tccTransactionId;
            return this;
        }

        public SimpleTCCManagerBuilder setRepositoryGear(IRepositoryGear repositoryGear) {
            this.repositoryGear = repositoryGear;
            return this;
        }

        public SimpleTCCManagerBuilder setSerializeGear(ISerializeGear serializeGear) {
            this.serializeGear = serializeGear;
            return this;
        }

        public SimpleTCCManagerBuilder setBusinessSupportGear(IBusinessSupportGear businessSupportGear) {
            this.businessSupportGear = businessSupportGear;
            return this;
        }

        public SimpleTCCManager build() {
            SimpleTCCManagerAdapter adapter = new SimpleTCCManagerAdapter();
            if(tccTransactionId != null && tccTransactionId.length() > 0) {
                SimpleTCCStateBiz tccStateBiz = new SimpleTCCStateBiz(repositoryGear, serializeGear);
                adapter.setTCCStateGetter(() -> tccStateBiz.call(tccTransactionId));
            }
            adapter.setStateManager(
                new SimpleStateManagerAdapter(
                    new SimpleTCCBeginBiz(repositoryGear, serializeGear, businessSupportGear), 
                    new SimpleTCCUpdateBiz(repositoryGear, serializeGear, businessSupportGear), 
                    new SimpleTCCEndBiz(repositoryGear, serializeGear, businessSupportGear))
            );
            adapter.setCompensateStrategy(
                new SimpleCompensateAdapter(
                    new SimpleTCCCompensateBiz(repositoryGear, serializeGear, businessSupportGear)
                )
            );
            
            return new SimpleTCCManager(adapter);
        }

    }
    

    public static class SimpleTCCManagerExtendBuilder extends SimpleTCCManagerBuilder {

        private SimpleTCCManagerExtendBuilder() {

        }

        private Func0<ITCCState> tccStateGetter;

        private SimpleTCCBeginBiz tccBeginBiz;

        private SimpleTCCUpdateBiz tccUpdateBiz;

        private SimpleTCCEndBiz tccEndBiz;

        private SimpleTCCCompensateBiz tccCompensateBiz;

        public SimpleTCCManagerExtendBuilder setTCCStateGetter(Func0<ITCCState> tccStateGetter) {
            this.tccStateGetter = tccStateGetter;
            return this;
        }

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

        public SimpleTCCManagerExtendBuilder setTCCCompensateBiz(SimpleTCCCompensateBiz tccCompensateBiz) {
            this.tccCompensateBiz = tccCompensateBiz;
            return this;
        }


        public SimpleTCCManager build() {
            SimpleTCCManagerAdapter adapter = new SimpleTCCManagerAdapter();
            adapter.setTCCStateGetter(tccStateGetter);
            adapter.setStateManager(
                new SimpleStateManagerAdapter(tccBeginBiz, tccUpdateBiz, tccEndBiz)
            );
            adapter.setCompensateStrategy(
                new SimpleCompensateAdapter(tccCompensateBiz)
            );
            
            return new SimpleTCCManager(adapter);
        }

    }
}
