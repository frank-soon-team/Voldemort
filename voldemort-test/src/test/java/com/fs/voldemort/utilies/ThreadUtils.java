package com.fs.voldemort.utilies;

public interface ThreadUtils {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
    
}
