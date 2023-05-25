package com.huang.lightweight.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties of LightWeightClient
 * Users can customize some client configurations
 * @author touwowo0320
 * @date 2023/05/24
 */
@ConfigurationProperties(prefix = "light.weight")
public class LightWeightClientProperties {
    /**
     * lightweight server address
     */
    private String serverAddress;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
