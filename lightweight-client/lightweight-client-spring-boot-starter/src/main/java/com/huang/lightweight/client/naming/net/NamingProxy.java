package com.huang.lightweight.client.naming.net;

import com.huang.lightweight.client.constant.UrlConstant;
import com.huang.lightweight.client.util.http.HttpClientUtil;
import com.huang.lightweight.client.util.http.HttpResult;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.common.util.common.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

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

        LoggerUtils.printIfInfoEnabled(logger, "[registering service {} with instance: {}", serviceName, instance);

        instance.setServiceName(serviceName);

        // Send a POST request to register the instance
        HttpResult httpResult = HttpClientUtil.getInstance().post(lightweightDomain + UrlConstant.instanceUrl, new HashMap<>(), instance);

        if (httpResult.getCode() == HttpStatus.SC_OK) {
            LoggerUtils.printIfInfoEnabled(logger, "register success");
        } else {
            LoggerUtils.printIfErrorEnabled(logger, "register fail, res = " + httpResult);
        }
    }

    private void initRefreshTask() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("com.huang.lightweight.client.naming.updater");
                t.setDaemon(true);
                return t;
            }
        });
    }
}
