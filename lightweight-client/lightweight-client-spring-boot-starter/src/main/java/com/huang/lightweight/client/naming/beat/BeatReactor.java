package com.huang.lightweight.client.naming.beat;

import com.huang.lightweight.client.constant.URLConstant;
import com.huang.lightweight.client.naming.net.NamingProxy;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BeatReactor {

    private final Logger logger = LoggerFactory.getLogger(BeatReactor.class);

    private final NamingProxy serverProxy;

    private final ScheduledExecutorService executorService;

    public BeatReactor(NamingProxy serverProxy, int threadCount) {
        this.serverProxy = serverProxy;
        executorService = new ScheduledThreadPoolExecutor(threadCount, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("com.huang.lightweight.naming.beat.sender");
            return thread;
        });
    }

    public void addBeatInfo(BeatInfo beatInfo) {
        LoggerUtils.printIfInfoEnabled(logger, "adding beat: {}", beatInfo);
        executorService.schedule(new BeatTask(beatInfo), beatInfo.getPeriod(), TimeUnit.MILLISECONDS);
    }

    class BeatTask implements Runnable {

        private final BeatInfo beatInfo;

        public BeatTask(BeatInfo beatInfo) {
            this.beatInfo = beatInfo;
        }

        @Override
        public void run() {
            Instance instance = new Instance();
            instance.setServiceName(beatInfo.getServiceName());
            instance.setIp(beatInfo.getIp());
            instance.setPort(beatInfo.getPort());
            serverProxy.sendBeat(instance);
            executorService.schedule(new BeatTask(beatInfo), beatInfo.getPeriod(), TimeUnit.MILLISECONDS);
        }
    }

}
