package com.huang.lightweight.liteconnect.balancer;

import com.huang.lightweight.common.pojo.instance.Instance;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于轮询的负载均衡器
 */
public class DefaultLoadBalancer extends AbstractLoadBalancer{

    /**
     * Spring context
     */
    private DiscoveryClient discoveryClient;

    /**
     * 记录轮询下标
     */
    private Map<String, AtomicInteger> selectMap;

    /**
     * 拿到服务发现客户端
     */
    public DefaultLoadBalancer(ApplicationContext applicationContext){
        this.discoveryClient = applicationContext.getBean(DiscoveryClient.class);
        selectMap = new ConcurrentHashMap<>();
    }

    /**
     * 获取服务名称对应的所有实例
     * @param serviceName
     * @return
     */
    @Override
    protected List<ServiceInstance> getInstances(String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }

    /**
     * 选择一个实例
     */
    @Override
    protected ServiceInstance selectInstance(String serviceName, List<ServiceInstance> instances) {
        AtomicInteger atomicInteger = selectMap.get(serviceName);
        if(atomicInteger == null){
            atomicInteger = new AtomicInteger(0);
        }
        // 自增加一
        atomicInteger.incrementAndGet();
        if(atomicInteger.get() >= instances.size()){
            atomicInteger.compareAndSet(atomicInteger.get(), 0);
        }
        selectMap.put(serviceName, atomicInteger);
        return instances.get(atomicInteger.get() % instances.size());
    }

}
