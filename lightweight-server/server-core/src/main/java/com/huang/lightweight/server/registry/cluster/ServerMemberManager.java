package com.huang.lightweight.server.registry.cluster;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.IPUtil;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.server.registry.cluster.net.ClusterProxy;
import com.huang.lightweight.server.registry.util.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * 管理集群节点
 *
 * @Author lightweight
 * @Date 2023/8/13 15:41
 */
@Component("serverMemberManager")
public class ServerMemberManager {

    private Logger logger = LoggerFactory.getLogger(ServerMemberManager.class);

    /**
     * 注册中心集群节点
     */
    private volatile ConcurrentSkipListMap<String, Member> serverList;

    /**
     * 注册中心集群节点(可正常使用)
     */
    private volatile ConcurrentSkipListMap<String, Member> aliveServerList;

    @Autowired
    private ClusterProxy clusterProxy;

    @Autowired
    private ServiceManager serverListManager;

    /**
     * IP
     */
    private String ip;

    /**
     * port.
     */
    @Value("${server.port}")
    private int port;

    /**
     * 当前节点的地址
     */
    private String localAddress;

    /**
     * 自己.
     */
    private volatile Member self;


    /**
     * 初始化自己这个节点
     * 从其他节点拉取实例信息
     */
    public void init() {
        serverList = new ConcurrentSkipListMap<>();
        aliveServerList = new ConcurrentSkipListMap<>();
        this.ip = IPUtil.getLocalIp();
        this.localAddress = IPUtil.getLocalIp() + ":" + port;
        this.self = Member.builder().ip(this.ip).port(this.port).state(NodeState.UP).build();
        serverList.put(this.localAddress, this.self);
        aliveServerList.put(this.localAddress, this.self);
        pullData();
    }

    /**
     * 拉取注册信息
     */
    private void pullData() {
        LoggerUtils.printIfDebugEnabled(logger, "pullData");
        Map<String, List<Instance>> map = clusterProxy.pullInstances();
        if (map != null) {
            serverListManager.replaceData(map);
        }
    }


    public Member getSelf() {
        return self;
    }

    public Member getFirstAliveNode() {
        return aliveServerList.firstEntry().getValue();
    }

    public List<Member> getMemberList() {
        return new ArrayList<>(serverList.values());
    }

    /**
     * 更新/新增 节点
     */
    public void updateMember(Member member) {
        if (serverList.containsKey(member.getAddress())) {
            serverList.get(member.getAddress()).setLastBeatTime(System.currentTimeMillis());
            return;
        }
        serverList.put(member.getAddress(), member);
    }

    /**
     * 只有第一个节点进行实例的心跳检测， 其他节点从第一个节点进行数据同步
     */
    public boolean checkFirstNode(Member member) {
        if (aliveServerList.firstEntry() == null) {
            return false;
        }
        return aliveServerList.firstEntry().getValue().getAddress().equals(member.getAddress());
    }

    /**
     * 判断节点状态 是否存活
     */
    public void checkMemberState() {
        for (Member member : serverList.values()) {
            if(member.equals(this.self)){
                member.setLastBeatTime(System.currentTimeMillis());
                continue;
            }
            long aliveTime = System.currentTimeMillis() - member.getLastBeatTime();
            if(aliveTime < Constants.HEART_BEAT_INTERVAL){
                member.setState(NodeState.UP);
            }
            if (aliveTime > Constants.HEART_BEAT_INTERVAL) {
                member.setState(NodeState.SUSPICIOUS);
            }
            if (aliveTime > Constants.HEART_BEAT_INTERVAL * 2) {
                member.setState(NodeState.DOWN);
            }
            if (member.getState() == NodeState.UP) {
                aliveServerList.put(member.getAddress(), member);
            } else {
                aliveServerList.remove(member.getAddress());
            }
        }
    }
}
