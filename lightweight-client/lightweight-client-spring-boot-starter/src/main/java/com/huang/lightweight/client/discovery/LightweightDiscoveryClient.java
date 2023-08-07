package com.huang.lightweight.client.discovery;

import com.huang.lightweight.common.exception.LightweightException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lightweight
 * @Date 2023/5/29 20:43
 */

@Configuration
public class LightweightDiscoveryClient implements DiscoveryClient {

    private LightweightServiceDiscovery serviceDiscovery;

    public LightweightDiscoveryClient(LightweightServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        try {
            return serviceDiscovery.getInstances(serviceId);
        } catch (LightweightException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getServices() {
        return null;
    }
}
