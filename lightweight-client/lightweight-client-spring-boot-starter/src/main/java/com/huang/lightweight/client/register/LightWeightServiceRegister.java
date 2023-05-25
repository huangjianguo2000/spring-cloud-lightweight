package com.huang.lightweight.client.register;

import com.huang.lightweight.client.LightWeightClientProperties;
import com.huang.lightweight.client.constant.UrlConstant;
import com.huang.lightweight.client.util.ServerUtil;
import com.huang.lightweight.client.util.http.HttpResult;
import com.huang.lightweight.client.util.http.RestClient;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;

/**
 * LightWeightServiceRegister
 *
 * @author 窝窝头
 * @date 2023/05/24
 */
public class LightWeightServiceRegister {

    private final Logger logger = LoggerFactory.getLogger(LightWeightServiceRegister.class);

    Environment env;

    private LightWeightClientProperties lightWeightClientProperties;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setLightWeightClientProperties(LightWeightClientProperties lightWeightClientProperties) {
        this.lightWeightClientProperties = lightWeightClientProperties;
    }

    /**
     * Obtain relevant configurations from the running environment for registration services
     */
    @PostConstruct
    public void registry() {
        Instance instance = new Instance();
        String serviceName = env.getProperty("spring.application.name", "");
        Integer servicePort = env.getProperty("server.port", Integer.class, 8080);
        InetAddress serverAddress = env.getProperty("server.address", InetAddress.class, null);
        instance.setInstanceId(UUID.randomUUID().toString());
        instance.setIp(Objects.nonNull(serverAddress) ? serverAddress.getHostAddress() : ServerUtil.getIpAddress());
        instance.setPort(servicePort);
        instance.setServiceName(serviceName);
        HttpResult httpResult = RestClient.getInstance().post(lightWeightClientProperties.getServerAddress() + UrlConstant.instanceUrl, null, instance);
        if(httpResult.getCode() == HttpStatus.SC_OK){
            LoggerUtils.printIfInfoEnabled(logger,"register success");
        }else {
            LoggerUtils.printIfErrorEnabled(logger,"register fail");
        }
    }
}
