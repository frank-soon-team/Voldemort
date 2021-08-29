package com.fs.voldemort.parallel;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelExecutorThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final String prefix;
    private final boolean daemon;
    private final ThreadGroup group;

    public ParallelExecutorThreadFactory(String name) {
        this(name, false);
    }

    public ParallelExecutorThreadFactory(String name, boolean daemon) {
        this.prefix = name + "-";
        this.daemon = daemon;

        SecurityManager securityManager = System.getSecurityManager();
        this.group = securityManager == null ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String threadName = prefix + threadNum.getAndIncrement();
        Thread thread = new Thread(group, runnable, threadName, 0);
        thread.setDaemon(daemon);
        return thread;
    }
    
}
