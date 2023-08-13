package com.huang.lightweight.server.registry.util;

import java.util.concurrent.*;

public class GlobalThreadPool {
    private static final int CORE_POOL_SIZE = 10;          // 核心线程数
    private static final int MAX_POOL_SIZE = 20;           // 最大线程数
    private static final long KEEP_ALIVE_TIME = 60L;       // 线程空闲时间
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS; // 时间单位
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>(); // 任务队列

    private static final ThreadFactory THREAD_FACTORY = new GlobalThreadFactory(); // 自定义线程工厂

    private static ThreadPoolExecutor globalThreadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, WORK_QUEUE, THREAD_FACTORY);

    public static void execute(Runnable task){
        globalThreadPool.execute(task);
    }

    // 自定义线程工厂
    private static class GlobalThreadFactory implements ThreadFactory {
        private static int threadCount = 0;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("GlobalThread-" + threadCount++);
            return thread;
        }
    }
}
