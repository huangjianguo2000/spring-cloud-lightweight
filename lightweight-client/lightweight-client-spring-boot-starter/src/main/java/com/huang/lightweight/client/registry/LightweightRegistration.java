package com.huang.lightweight.client.registry;

import com.huang.lightweight.client.LightweightDiscoveryProperties;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.Map;

/**
 * LightweightRegistration
 *
 * @Author lightweight
 * @Date 2023/5/23 14:34
 */public class LightweightRegistration implements Registration {

    /**
     * Information about the service and configuration file
     */
    private final LightweightDiscoveryProperties lightWeightDiscoveryProperties;

    private Map<String, String> metaData;

    public LightweightRegistration(LightweightDiscoveryProperties lightWeightDiscoveryProperties) {
        this.lightWeightDiscoveryProperties = lightWeightDiscoveryProperties;
    }

    /**
     * Initializes the LightweightRegistration.
     */
    public void init() {
        // init metaData
    }

    @Override
    public String getServiceId() {
        return lightWeightDiscoveryProperties.getService();
    }

    @Override
    public String getHost() {
        return lightWeightDiscoveryProperties.getIp();
    }

    @Override
    public int getPort() {
        return lightWeightDiscoveryProperties.getPort();
    }

    /**
     * Sets the port for the registration.
     *
     * @param port The port value to be set.
     */
    public void setPort(Integer port) {
        lightWeightDiscoveryProperties.setPort(port);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metaData;
    }


    /**
     * Retrieves the LightweightDiscoveryProperties associated with the registration.
     *
     * @return The LightweightDiscoveryProperties object.
     */
    public LightweightDiscoveryProperties getLightWeightDiscoveryProperties() {
        return lightWeightDiscoveryProperties;
    }


}
