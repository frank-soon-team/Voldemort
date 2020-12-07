# Voldemort

[![Badge](https://img.shields.io/badge/link-996.icu-%23FF4D5B.svg?style=flat-square)](https://996.icu/#/zh_CN)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg?style=flat-square)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

<hr />

<br />

## Caller
```
public class Caller
```

## BusinessCaller
```
public class BusinessCaller extends Caller
```

## TCCCaller 
```
public class TCCCaller extends BusinessCaller
```

## AsyncCaller
```
public class AsyncCaller extends Caller
```

## Voldemort
```
// Lambda调用链
Voldemort.caller().call().exec();

// 函数调用链
Voldemort.businessCaller().call().exec();

// TCC事务链
Voldemort.tccCaller().call().exec();

// 多事务链
Voldemort.caller()
    .call(
        Voldmort.tccCaller().call(ITCCNode.class)
    )
    .call(
        Voldmort.tccCaller().call(ITCCNode.class)
    )
    .exec();
```
