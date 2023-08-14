package com.huang.lightweight.server.registry.cluster.beat;

import com.huang.lightweight.server.registry.cluster.net.ClusterProxy;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务端发送心跳检测
 * @Author lightweight
 * @Date 2023/8/13 20:12
 */

public class ServerBeatReactor {

    private ClusterProxy clusterProxy;

    private final ScheduledExecutorService executorService;

    public ServerBeatReactor(ClusterProxy clusterProxy, int threadCount) {
        this.clusterProxy = clusterProxy;
        executorService = new ScheduledThreadPoolExecutor(threadCount, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("com.huang.lightweight.naming.beat.sender");
            return thread;
        });
    }
    public void addBeatInfo(ServerBeatInfo serverBeatInfo) {
        executorService.scheduleWithFixedDelay(new BeatTask(serverBeatInfo), 0, serverBeatInfo.getPeriod(), TimeUnit.MILLISECONDS);
    }

    class BeatTask implements Runnable {

        private final ServerBeatInfo serverBeatInfo;

        public BeatTask(ServerBeatInfo beatInfo) {
            this.serverBeatInfo = beatInfo;
        }

        @Override
        public void run() {
            clusterProxy.sendBeat(serverBeatInfo.getMemberRequest());
        }
    }

}
