package com.huang.lightweight.client.discovery;

import com.huang.lightweight.client.LightweightDiscoveryProperties;
import com.huang.lightweight.client.LightweightServiceInstance;
import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

import java.util.*;

/**
 * @Author lightweight
 * @Date 2023/5/29 20:46
 */
public class LightweightServiceDiscovery {

    private final Logger logger = LoggerFactory.getLogger(LightweightServiceDiscovery.class);

    private LightweightDiscoveryProperties discoveryProperties;

    public LightweightServiceDiscovery(LightweightDiscoveryProperties lightweightDiscoveryProperties) {
        this.discoveryProperties = lightweightDiscoveryProperties;
    }

    /**
     *  获取某个服务集群下的实例列表
     * @param serviceId
     * @return
     * @throws LightweightException
     */
    public List<ServiceInstance> getInstances(String serviceId) throws LightweightException {
        List<Instance> instances = this.discoveryProperties.getNamingService().getAllInstances(serviceId);
        LoggerUtils.printIfInfoEnabled(logger, "discovery getInstances list = {}", instances);
        return hostToServiceInstanceList(instances, serviceId);
    }

    public static List<ServiceInstance> hostToServiceInstanceList(
            List<Instance> instances, String serviceId) {
        List<ServiceInstance> result = new ArrayList<>(instances.size());
        for (Instance instance : instances) {
            ServiceInstance serviceInstance = hostToServiceInstance(instance, serviceId);
            if (serviceInstance != null) {
                result.add(serviceInstance);
            }
        }
        return result;
    }

    public static ServiceInstance hostToServiceInstance(Instance instance,
                                                        String serviceId) {
        if (instance == null || !instance.isEnabled() || !instance.isHealthy()) {
            return null;
        }
        LightweightServiceInstance lightweightServiceInstance = new LightweightServiceInstance();
        lightweightServiceInstance.setHost(instance.getIp());
        lightweightServiceInstance.setPort(instance.getPort());
        lightweightServiceInstance.setServiceId(serviceId);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("nacos.instanceId", instance.getInstanceId());
        metadata.put("nacos.weight", instance.getWeight() + "");
        metadata.put("nacos.healthy", instance.isHealthy() + "");
        metadata.put("nacos.cluster", instance.getClusterName() + "");
        metadata.putAll(instance.getMetadata());
        lightweightServiceInstance.setMetadata(metadata);

        return lightweightServiceInstance;
    }
}
