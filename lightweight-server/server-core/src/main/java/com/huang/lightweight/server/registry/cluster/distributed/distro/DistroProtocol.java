package com.huang.lightweight.server.registry.cluster.distributed.distro;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.task.TaskExecuteWorker;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.server.registry.cluster.ServerMemberManager;
import com.huang.lightweight.server.registry.cluster.beat.MemberHeartbeat;
import com.huang.lightweight.server.registry.cluster.beat.ServerBeatInfo;
import com.huang.lightweight.server.registry.cluster.beat.ServerBeatReactor;
import com.huang.lightweight.server.registry.cluster.net.ClusterProxy;
import com.huang.lightweight.server.registry.entity.MemberRequest;

import com.huang.lightweight.server.registry.util.GlobalThreadPool;
import com.huang.lightweight.server.registry.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author lightweight
 * @Date 2023/8/13 20:45
 */
@Component
public class DistroProtocol {

    private final Logger logger = LoggerFactory.getLogger(DistroProtocol.class);

    @Autowired
    private ServerMemberManager serverMemberManager;

    @Autowired
    private ClusterProxy clusterProxy;

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private MemberHeartbeat memberHeartbeat;

    private TaskExecuteWorker taskExecuteWorker;

    public void init() {
        serverMemberManager.init();
        ServerBeatReactor serverBeatReactor = new ServerBeatReactor(clusterProxy, 1);

        // 增加心跳任务 发送心跳
        ServerBeatInfo serverBeatInfo = new ServerBeatInfo();
        MemberRequest memberRequest = new MemberRequest(serverMemberManager.getSelf());

        serverBeatInfo.setMemberRequest(memberRequest);
        serverBeatInfo.setServiceManager(serviceManager);
        serverBeatInfo.setPeriod(Constants.DEFAULT_HEART_BEAT_INTERVAL);
        serverBeatReactor.addBeatInfo(serverBeatInfo);
        // 检测心跳
        memberHeartbeat.checkState(serverMemberManager);
        // 任务执行者
        taskExecuteWorker = new TaskExecuteWorker("send registerInstance");
    }

    /**
     * 接收心跳 1. 更新集群状态 2. 更新当前节点的实例信息
     */
    public void beatCheck(MemberRequest memberRequest) {
        //LoggerUtils.printIfDebugEnabled(logger, "beatCheck listInstance = {}", memberRequest.getInstances());
        // 是第一个节点 直接使用他的实例信息
        if (serverMemberManager.checkFirstNode(memberRequest.getMember())) {
            serviceManager.replaceData(memberRequest.convertToMap());
        }
        // 自己是第一个节点 更新心跳消息
        if (serverMemberManager.checkFirstNode(serverMemberManager.getSelf())) {
            updateBeatTime(memberRequest.getInstances());
        }
        serverMemberManager.updateMember(memberRequest.getMember());
    }

    /**
     * 从其他节点更新实例心跳检测时间
     */
    public synchronized void updateBeatTime(List<InstanceWrapper> list) {
        //LoggerUtils.printIfDebugEnabled(logger, "updateBeatTime list = {}", list);
        Map<String, Instance> myInstanceMap = serviceManager.listAsMap();
        for (InstanceWrapper instanceWrapper : list) {
            for (Instance host : instanceWrapper.getHosts()) {
                Instance instance = myInstanceMap.get(host.getIp() + host.getPort());
                if (instance != null && host.getLastBeat() > instance.getLastBeat()) {
                    serviceManager.update(host.getServiceName(), host);
                }
            }
        }
    }

    /**
     * 将注册信息发送给第一个节点
     */
    public void sendRegisterInstance(Instance instance) {
        if (!serverMemberManager.checkFirstNode(serverMemberManager.getSelf())) {
//            LoggerUtils.printIfDebugEnabled(logger, "sendRegisterInstance {}, instance = {}",
//                    serverMemberManager.getFirstAliveNode().getCompleteAddress() +
//                            Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/instance", instance);
            GlobalThreadPool.execute(() -> {
                // 循环直到消息发送给第一个节点成功
                while (true) {
                    if (clusterProxy.sendRegisterInstance(serverMemberManager.
                            getFirstAliveNode().getCompleteAddress() + Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT
                            + "/instance", instance)) {
                        break;
                    }
                }
            });
        }
    }
}
