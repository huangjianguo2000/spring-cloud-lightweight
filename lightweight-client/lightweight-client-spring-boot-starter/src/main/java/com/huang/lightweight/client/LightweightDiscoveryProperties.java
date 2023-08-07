package com.huang.lightweight.client;

import com.huang.lightweight.client.factory.PropertyKeyConst;
import com.huang.lightweight.client.naming.NamingFactory;
import com.huang.lightweight.client.naming.NamingService;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;

/**
 * Properties of LightWeightClient
 * Users can customize some client configurations
 *
 * @author touwowo0320
 * @date 2023/05/24
 */
@ConfigurationProperties(prefix = "spring.cloud.lightweight.discovery")
public class LightweightDiscoveryProperties {
    private static final Logger logger = LoggerFactory.getLogger(LightweightDiscoveryProperties.class);
    /**
     * lightweight server address
     */
    private String serverAddress;

    private String ip;

    private Integer port = -1;

    @Value("${spring.cloud.lightweight.discovery.service:${spring.application.name:}}")
    private String service;

    @Autowired
    private InetUtils inetUtils;

    private boolean registerEnabled = true;

    private static NamingService namingService;

    @PostConstruct
    public void init() {

        this.serverAddress = Objects.toString(this.serverAddress, "");
        if (this.serverAddress.endsWith("/")) {
            this.serverAddress = this.serverAddress.substring(0, this.serverAddress.length() - 1);
        }

        if (StringUtils.isEmpty(ip)) {
            // traversing network interfaces if didn't specify a interface
            ip = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isRegisterEnabled() {
        return registerEnabled;
    }

    public void setRegisterEnabled(boolean registerEnabled) {
        this.registerEnabled = registerEnabled;
    }

    public NamingService getNamingService() {
        if (namingService != null) {
            return namingService;
        }
        try {
            namingService = NamingFactory.createNamingService(getLightweightProperties());
        } catch (Exception e) {
            LoggerUtils.printIfErrorEnabled(logger, "create naming service error e = " + e.toString());
        }
        return namingService;
    }

    public static void setNamingService(NamingService namingService) {
        LightweightDiscoveryProperties.namingService = namingService;
    }

    private Properties getLightweightProperties() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDRESS, serverAddress);
        return properties;
    }
}
