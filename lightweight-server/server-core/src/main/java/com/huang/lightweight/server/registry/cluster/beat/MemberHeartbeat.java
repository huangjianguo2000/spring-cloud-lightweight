package com.huang.lightweight.server.registry.cluster.beat;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.server.registry.cluster.ServerMemberManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author lightweight
 * @Date 2023/8/14 14:47
 */
@Component
public class MemberHeartbeat {

    private final Logger logger = LoggerFactory.getLogger(InstanceHeartbeat.class);

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    /**
     * 初始化 ScheduledThreadPool 组件
     */
    @PostConstruct
    public void init() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    public void checkState(ServerMemberManager serverMemberManager) {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(serverMemberManager::checkMemberState,
                0, Constants.DEFAULT_HEART_BEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

}
