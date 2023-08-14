package com.huang.lightweight.server.registry.cluster.net;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.common.util.http.HttpClientUtil;
import com.huang.lightweight.common.util.http.HttpResult;
import com.huang.lightweight.server.registry.entity.MemberRequest;
import com.huang.lightweight.server.registry.util.GlobalThreadPool;
import jdk.jfr.internal.tool.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author lightweight
 * @Date 2023/8/13 19:29
 */
@Component
public class ClusterProxy {

    @Value("${lightweight.cluster.address:none}")
    private String IPListString;

    @Value("${server.port}")
    private int port;

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
        IPList = new CopyOnWriteArrayList<>();
        for (String address : addresses) {
            String[] subAddress = address.split(":");
            if (subAddress[1].equals(String.valueOf(port))
                    && subAddress[0].equals("localhost")) {
                continue;
            }
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
            String curl = url + Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/instance";
            HttpResult httpResult = HttpClientUtil.getInstance().get(curl);
            // 请求失败
            if (httpResult.getCode() != 200) {
                continue;
            }
            String body = httpResult.getBody();
            LoggerUtils.printIfDebugEnabled(logger, "pull instances {}", body);
            Result<List<InstanceWrapper>> result = JSON.parseObject(body, Result.class);
            // 请求失败
            if(result.getCode() != 0){
                continue;
            }
            List<InstanceWrapper> instanceWrappers = result.getData();
            LoggerUtils.printIfDebugEnabled(logger, "instanceWrappers = {}", instanceWrappers);
            ConcurrentHashMap<String, List<Instance>> ans = new ConcurrentHashMap<>();
            if(instanceWrappers == null){
                return ans;
            }
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
            if (IPList != null) {
                for (String url : IPList) {
                    HttpClientUtil.getInstance().post(url + Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/distro", data);
                  //  LoggerUtils.printIfDebugEnabled(logger, "beat send {}, to {}", memberRequest, url + Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/distro");
                }
            }
        });
    }

    /**
     * 将注册信息发送给第一个节点
     */
    public void sendRegisterInstance(String url, Instance instance) {
        HttpResult post = HttpClientUtil.getInstance().post(url, instance);
        String body = post.getBody();
        System.out.println(body);
//        if(post.getCode())
    }

}
