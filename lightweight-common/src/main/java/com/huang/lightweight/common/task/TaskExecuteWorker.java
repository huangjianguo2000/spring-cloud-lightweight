package com.huang.lightweight.common.task;

import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author lightweight
 * @Date 2023/8/14 15:53
 */
public class TaskExecuteWorker {

    private final Logger logger = LoggerFactory.getLogger(TaskExecuteWorker.class);

    /**
     * Max task queue size 32768.
     */
    private static final int QUEUE_CAPACITY = 1 << 15;

    private final BlockingQueue<Runnable> queue;

    private final AtomicBoolean closed;

    public TaskExecuteWorker(final String name) {
        this.queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        this.closed = new AtomicBoolean(false);
        new InnerWorker(name).start();
    }

    private void putTask(Runnable task) {
        try {
            queue.put(task);
        } catch (InterruptedException ire) {
            LoggerUtils.printIfErrorEnabled(logger, ire.toString(), ire);
        }
    }

    /**
     * Inner execute worker.
     */
    private class InnerWorker extends Thread {

        InnerWorker(String name) {
            setDaemon(false);
            setName(name);
        }

        @Override
        public void run() {
            while (!closed.get()) {
                try {
                    Runnable task = queue.take();
                    long begin = System.currentTimeMillis();
                    task.run();
                    long duration = System.currentTimeMillis() - begin;
                    if (duration > 1000L) {
                        LoggerUtils.printIfWarnEnabled(logger, "distro task {} takes {}ms", task, duration);
                    }
                } catch (Throwable e) {
                    LoggerUtils.printIfErrorEnabled(logger, "[DISTRO-FAILED] " + e.toString(), e);
                }
            }
        }
    }
}
