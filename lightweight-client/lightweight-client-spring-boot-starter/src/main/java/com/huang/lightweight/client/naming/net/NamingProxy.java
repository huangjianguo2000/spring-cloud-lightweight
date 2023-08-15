package com.huang.lightweight.client.naming.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.huang.lightweight.client.constant.URLConstant;
import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.util.http.HttpClientUtil;
import com.huang.lightweight.common.util.http.HttpResult;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.common.util.common.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NamingProxy {

    private final Logger logger = LoggerFactory.getLogger(NamingProxy.class);

    /**
     * cluster server list
     */
    private List<String> serverList;

    /**
     * standalone -->  server ip
     */
    private String lightweightDomain;

    /**
     * data key-map
     */
    private Properties properties;

    /**
     * init
     *
     * @param serverList server ip
     * @param properties data
     */
    public NamingProxy(String serverList, Properties properties) {
        if (StringUtils.isNotEmpty(serverList)) {
            // Split the serverList string into a list of servers
            this.serverList = Arrays.asList(serverList.split(","));
            if (this.serverList.size() == 1) {
                this.lightweightDomain = serverList;
            }
        }
        this.properties = properties;
    }

    /**
     * Registers a service with the naming service.
     *
     * @param serviceName The name of the service.
     * @param instance    The instance object to be registered.
     */
    public void registerService(String serviceName, Instance instance) {

        LoggerUtils.printIfInfoEnabled(logger, "[registering service {} with instance: {}]", serviceName, instance);

        instance.setServiceName(serviceName);

        // Send a POST request to register the instance
        HttpResult httpResult = HttpClientUtil.getInstance().post(lightweightDomain + URLConstant.instanceUrl, new HashMap<>(), instance);

        if (httpResult.getCode() == HttpStatus.SC_OK) {
            LoggerUtils.printIfInfoEnabled(logger, "register success");
        } else {
            LoggerUtils.printIfErrorEnabled(logger, "register fail, res = " + httpResult);
        }
    }

    public void sendBeat(Instance instance) {
        String url = lightweightDomain + URLConstant.SEND_BEAT;
        HttpResult httpResult = HttpClientUtil.getInstance().post(url, instance);
        if (httpResult.getCode() != HttpStatus.SC_OK) {
            LoggerUtils.printIfWarnEnabled(logger, "send beat fail, url = {} instance = {} res = {}",
                    url, instance, httpResult);
        }
    }

    /**
     * 查询注册中心的所有服务实例
     *
     * @return res
     */
    public Map<String, List<Instance>> listInstance() {
        HttpResult httpResult = HttpClientUtil.getInstance().get(lightweightDomain + URLConstant.instanceUrl);
        Map<String, List<Instance>> ans = new HashMap<>();
        if (httpResult.getCode() == HttpStatus.SC_OK) {
            String body = httpResult.getBody();
            JSONObject json = JSON.parseObject(body);
            JSONArray dataArray = json.getJSONArray("data");
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                String serviceName = item.getString("serviceName");
                JSONArray hostsArray = item.getJSONArray("hosts");
                List<Instance> instances = JSON.parseArray(hostsArray.toJSONString(), Instance.class);
                ans.put(serviceName, instances);
            }
        } else {
            LoggerUtils.printIfInfoEnabled(logger, "list instance error, res = " + httpResult);
        }
        return ans;
    }
}
