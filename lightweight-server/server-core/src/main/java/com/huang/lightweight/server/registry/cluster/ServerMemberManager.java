package com.huang.lightweight.server.registry.cluster;

import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.IPUtil;
import com.huang.lightweight.server.registry.cluster.net.ClusterProxy;
import com.huang.lightweight.server.registry.util.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

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
        this.ip = IPUtil.getLocalIp();
        this.localAddress = IPUtil.getLocalIp() + ":" + port;
        this.self = Member.builder().ip(this.ip).port(this.port).build();
        serverList.put(this.localAddress, this.self);

        pullData();
    }

    /**
     * 拉取注册信息
     */
    private void pullData() {
        Map<String, List<Instance>> map = clusterProxy.pullInstances();
        if (map != null) {
            serverListManager.replaceData(map);
        }
    }


    public Member getSelf() {
        return self;
    }
}
