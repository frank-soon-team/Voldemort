package com.fs.voldemort.tcc;

import com.fs.voldemort.core.Caller;
import com.fs.voldemort.core.functional.action.Action1;
import com.fs.voldemort.tcc.node.ITCCHandler;
import com.fs.voldemort.tcc.node.TCCNodeParameter;
import com.fs.voldemort.tcc.state.ITCCState;
import com.fs.voldemort.tcc.state.TCCExecuteState;

/**
 * TCC 协调器
 * <pre>
 * // 用法：
 *   TCCManager tccManager = new TCCManager(buildManagerAdapter());
 *   String message = "Welcome to XXX";
 *   List<UserInfo> userList = loadUserList();
 * 
 *   TCCCaller caller = new TCCCaller(tccManager, message);
 *   for(UserInfo userInfo : userList) {
 *      String nodeName = "SendMessageToUser-" + userInfo.getTenantCode();
 *      caller.call(
 *          nodeName, 
 *          p -> sendMessage(userInfo.getTenantCode(), userInfo.getUserCode(), (String) p.getParam()),
 *          p -> sendCompleted(userInfo.getTenantCode(), userInfo.getUserCode(), (String) p.getParam()),
 *          p -> cancelSend(userInfo.getTenantCode(), userInfo.getUserCode(), (String) p.getParam())
 *      );
 *   }
 *   caller.exec();
 * </pre>
 */
public class TCCCaller extends Caller<Void> {

    public TCCCaller(TCCManager tccManager) {
        super(tccManager);
    }

    public TCCCaller(TCCManager tccManager, Object param) {
        super(tccManager);
        call(p -> {
            ITCCState tccState = ((TCCNodeParameter) p).getTCCState();
            boolean isCompensation = 
                tccState instanceof TCCExecuteState
                    ? ((TCCExecuteState) tccState).isCompensation()
                    : false;
            if(!isCompensation) {
                tccState.setParam(param);
            }
            return null;
        });
    }

    public TCCCaller call(String name, ITCCHandler tccHandler) {
        if(tccHandler == null) {
            throw new IllegalArgumentException("the parameter [tccHandler] is required.");
        }

        getTCCManager().add(name, tccHandler);
        return this;
    }

    public TCCCaller call(
        String name, 
        Action1<TCCNodeParameter> tryFn,
        Action1<TCCNodeParameter> cancelFn) {

        return call(name, tryFn, null, cancelFn);
    }

    public TCCCaller call(
        String name, 
        Action1<TCCNodeParameter> tryFn,
        Action1<TCCNodeParameter> confirmFn,
        Action1<TCCNodeParameter> cancelFn) {

        if(tryFn == null) {
            throw new IllegalArgumentException("the parameter [tryFn] is required.");
        }
        if(cancelFn == null) {
            throw new IllegalArgumentException("the parameter [cancelFn] is required.");
        }
        
        getTCCManager().add(name, new ITCCHandler() {

            @Override
            public void goTry(TCCNodeParameter parameter) {
                tryFn.apply(parameter);
            }

            @Override
            public void confirm(TCCNodeParameter parameter) {
                if(confirmFn != null) {
                    confirmFn.apply(parameter);
                }
            }

            @Override
            public void cancel(TCCNodeParameter parameter) {
                cancelFn.apply(parameter);
            }
        });
        return this;
    }

    public TCCManager getTCCManager() {
        return (TCCManager) this.funcList;
    }
    
}
