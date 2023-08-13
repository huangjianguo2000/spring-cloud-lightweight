package com.huang.lightweight.server.registry.cluster.distributed.distro;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.server.registry.cluster.ServerMemberManager;
import com.huang.lightweight.server.registry.cluster.beat.ServerBeatInfo;
import com.huang.lightweight.server.registry.cluster.beat.ServerBeatReactor;
import com.huang.lightweight.server.registry.cluster.net.ClusterProxy;
import com.huang.lightweight.server.registry.entity.MemberRequest;
import com.huang.lightweight.server.registry.service.InstanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author lightweight
 * @Date 2023/8/13 20:45
 */
@Component
public class DistroProtocol {

    @Autowired
    private ServerMemberManager serverMemberManager;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private ClusterProxy clusterProxy;

    private ServerBeatReactor serverBeatReactor;

    public void init() {

        serverMemberManager.init();
        serverBeatReactor = new ServerBeatReactor(clusterProxy, 1);

        // 增加心跳任务
        ServerBeatInfo serverBeatInfo = new ServerBeatInfo();
        MemberRequest memberRequest = new MemberRequest(serverMemberManager.getSelf(), instanceService.listInstances());
        serverBeatInfo.setMemberRequest(memberRequest);
        serverBeatInfo.setPeriod(Constants.DEFAULT_HEART_BEAT_INTERVAL);
        serverBeatReactor.addBeatInfo(serverBeatInfo);
    }

}
