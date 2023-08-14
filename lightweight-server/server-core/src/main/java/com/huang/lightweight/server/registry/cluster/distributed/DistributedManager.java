package com.huang.lightweight.server.registry.cluster.distributed;

import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.server.registry.cluster.distributed.distro.DistroProtocol;
import com.huang.lightweight.server.registry.util.GlobalThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 决策是否使用集群架构
 *
 * @Author lightweight
 * @Date 2023/8/13 21:24
 */
@Component
public class DistributedManager {

    private final Logger logger = LoggerFactory.getLogger(DistributedManager.class);

    @Value("${lightweight.cluster.address:none}")
    private String IPListString;

    @Value("${lightweight.cluster.mode:none}")
    private String clusterMode;

    private DistributedMode mode = DistributedMode.STANDALONE;

    @Autowired
    private DistroProtocol distroProtocol;

    @PostConstruct
    void init() {
        if ("none".equals(IPListString)) {
            LoggerUtils.printIfInfoEnabled(logger, "start with standalone mode");
            return;
        }
        if (clusterMode.equals("cp")) {
            mode = DistributedMode.CP;
            return;
        }
        mode = DistributedMode.AP;
        LoggerUtils.printIfInfoEnabled(logger, "start with cluster-ap mode");
        GlobalThreadPool.execute(distroProtocol::init);
    }

    public DistributedMode getMode() {
        return mode;
    }
}
