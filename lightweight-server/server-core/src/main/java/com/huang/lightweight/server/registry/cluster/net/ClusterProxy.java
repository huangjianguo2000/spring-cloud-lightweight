package com.huang.lightweight.server.registry.cluster.net;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.common.util.http.HttpClientUtil;
import com.huang.lightweight.common.util.http.HttpResult;
import com.huang.lightweight.server.registry.entity.MemberRequest;
import com.huang.lightweight.server.registry.util.GlobalThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lightweight
 * @Date 2023/8/13 19:29
 */
@Component
public class ClusterProxy {

    @Value("${lightweight.cluster.address:none}")
    private String IPListString;

    private List<String> IPList = null;

    private final Logger logger = LoggerFactory.getLogger(ClusterProxy.class);


    @PostConstruct
    public void ClusterProxy() throws LightweightException {
        if ("none".equals(IPListString)) {
            return;
        }
        if (IPListString == null) {
            throw new LightweightException(ErrorCode.CLUSTER_ADD_ERROR, "cluster address is null");
        }
        String trimmedIPListString = IPListString.replaceAll("\\s+", ""); // 去除空格
        String[] addresses = trimmedIPListString.split(",");
        IPList = new ArrayList<>();
        for (String address : addresses) {
            IPList.add(Constants.NET_PROTOCOL + address);
        }
        LoggerUtils.printIfInfoEnabled(logger, "IPList: {}", IPList);
    }

    /**
     * 从其他节点拉取服务列表
     *
     * @return
     */
    public Map<String, List<Instance>> pullInstances() {
        for (String url : IPList) {
            HttpResult httpResult = HttpClientUtil.getInstance().get(url + "/instance");
            if (httpResult.getCode() != 200) {
                continue;
            }
            String body = httpResult.getBody();
            List<InstanceWrapper> instanceWrappers = JSON.parseArray(body, InstanceWrapper.class);
            ConcurrentHashMap<String, List<Instance>> ans = new ConcurrentHashMap<>();
            for (InstanceWrapper instanceWrapper : instanceWrappers) {
                List<Instance> hosts = instanceWrapper.getHosts();
                ans.put(instanceWrapper.getServiceName(), hosts);
            }
            return ans;
        }
        return new ConcurrentHashMap<>();
    }

    /**
     * 发送心跳
     */
    public void sendBeat(MemberRequest memberRequest) {
        String data = JSON.toJSONString(memberRequest);
        GlobalThreadPool.execute(() -> {
            HttpClientUtil.getInstance().post(Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/distro", data);
        });
    }

}
