package com.fs.voldemort.business.fit;

import com.fs.voldemort.business.support.BFuncOperate;
import com.fs.voldemort.core.functional.func.Func2;
import com.fs.voldemort.core.support.CallerParameter;

public enum FitMode {

    /*
     * # Definition
     *  R func(
     *      Part 1-> Result arg
     *      Part 2-> Context arg
     *      Part 3-> Operate function
     *  )
     *
     * # Part1 & Part2
     * Get arg info of target function method, and adapt result that has been executed by last function:
     * ## Role 1
     *     Last function    call    Current function
     *     R func1()        --->    func2(R arg)
     * ## Role 2
     *     Last function    call    Current function
     *     R func1()        --->    func2(R arg,   C1 arg1, C2 arg2, C2 arg3)
     *  ### Parameter description
     *     R arg                    -> The result of last function
     *     C1 arg1  context param   -> The arg of context param
     *                                 Context param class is C1.class
     *                                 Context param key is     'arg1'
     *     C2 arg2                  -> Class: C1.class      key:'arg2'
     *     C2 arg3                  -> Class: C2.class      key:'arg3'
     *
     * #Part3
     * The above has been described the role which fill the result and parameters into the current function,
     * but there is still such a scene, developer need to manipulate context parameters in the function, that`s
     * why BFunc provides the entry of the operation function in the formal parameter;
     * For example base on above # role 2 current function:
     *  func2(R arg,  C1 arg1, C2 arg2, C2 arg3, {@link BFuncOperate} Func2<String,Object,Boolean> f_setC)
     *
     */
    AUTO(FitLibrary.AUTO_FIT_FUNC),

    CUSTOM(FitLibrary.CUSTOM_FIT_FUNC);

    private final Func2<Class<?>,CallerParameter,Object[]> fitFunc;

    FitMode(Func2<Class<?>,CallerParameter, Object[]> fitFunc) {
        this.fitFunc = fitFunc;
    }

    public Func2<Class<?>, CallerParameter, Object[]> getFitFunc() {
        return fitFunc;
    }
}
