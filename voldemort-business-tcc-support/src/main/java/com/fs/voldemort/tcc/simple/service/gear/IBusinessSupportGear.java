package com.fs.voldemort.tcc.simple.service.gear;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

import com.fs.voldemort.core.functional.func.DynamicFunc;
import com.fs.voldemort.core.functional.func.Func1;
import com.fs.voldemort.core.support.CallerContext;

public interface IBusinessSupportGear {

    String getTraceID(CallerContext context);

    String getBizCode(CallerContext context);

    public static void main(String[] args) throws Exception {

        DynamicFunc<Object> func = putFunc(a -> a + "::MethodName");
        System.out.println(func.call("Func1"));

        // Method[] methods = func.getClass().getMethods();
        // Method callMethod = null;
        // for(int i = 0; i < methods.length; i++) {
        //     Method m = methods[i];
        //     if(m.getName().equals("call")) {
        //         callMethod = m;
        //     }
        // }

        // Method writeReplaceMethod = func.getClass().getDeclaredMethod("writeReplace");
        // writeReplaceMethod.setAccessible(true);
        // SerializedLambda serializedLambda = (SerializedLambda)writeReplaceMethod.invoke(func);
        // System.out.println(serializedLambda.getCapturedArgCount());
        

        // System.out.println(callMethod.canAccess(func));
        // callMethod.setAccessible(false);
        // String obj = (String) callMethod.invoke(func, "Func1<Object, Object>", 1);
        // System.out.println(obj);

    }

    @SuppressWarnings("unchecked")
    public static <T, R> DynamicFunc<R> putFunc(Func1<T, R> func) {
        DynamicFunc<R> dfunc = args -> func.call((T) args[0]);
        return dfunc;
    }
    
}
