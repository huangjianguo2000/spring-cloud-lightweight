package com.huang.lightweight.client.naming.net;

import com.huang.lightweight.client.constant.URLConstant;
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
     *  cluster server list
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

    public void sendBeat(Instance instance){
        LoggerUtils.printIfDebugEnabled(logger, "[send beat {} with instance: {}]", instance);
        HttpResult httpResult = HttpClientUtil.getInstance().post(lightweightDomain + URLConstant.SEND_BEAT, new HashMap<>(), instance);
        if (httpResult.getCode() == HttpStatus.SC_OK) {
            LoggerUtils.printIfDebugEnabled(logger, "send beat success");
        } else {
            LoggerUtils.printIfInfoEnabled(logger, "send beat fail, res = " + httpResult);
        }
    }
}
