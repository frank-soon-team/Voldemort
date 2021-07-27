package com.fs.voldemort.core.business.fit;

import com.fs.voldemort.business.fit.CArg;
import com.fs.voldemort.business.fit.IFit;
import com.fs.voldemort.business.fit.PArg;
import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.core.support.CallerParameter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomFit implements IFit {

    public Object[] fit(Method funcMethod, CallerParameter p){
        //arg result set
        final List<Object> arg = new LinkedList<>();
        //Context arg temporary set
        final List<CArg> cArgSet = new LinkedList<>();
        //Param arg temporary set
        final List<PArg> pArgSet = Arrays.stream(funcMethod.getParameters()).filter(param->{
            if(param.isAnnotationPresent(BFuncOperate.class)){
                cArgSet.add(new CArg(param.getAnnotation(BFuncOperate.class).value(),p.context()));
                return false;
            }
            return true;
        }).map(param-> new PArg(param.getName())).collect(Collectors.toList());

        if(!pArgSet.isEmpty()) {
            //Deal result arg
            final PArg resultArg = pArgSet.iterator().next();
            resultArg.value = p.result;

            //Deal context arg
            if(pArgSet.size()>1) {
                pArgSet.stream().skip(1).forEach(pArg -> pArg.value = p.context().get(pArg.name));
            }
            arg.addAll(pArgSet.stream().map(pArg -> pArg.value).collect(Collectors.toList()));
        }

        //Deal context operator func arg
        if(!cArgSet.isEmpty()) {
            arg.addAll(cArgSet.stream().map(CArg::getOperFunc).collect(Collectors.toList()));
        }

        return arg.toArray();
    }

}
