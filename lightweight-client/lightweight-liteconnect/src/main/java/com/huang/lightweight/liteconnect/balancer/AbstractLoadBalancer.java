package com.huang.lightweight.liteconnect.balancer;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * 定义模板方法
 */
public  abstract class AbstractLoadBalancer implements LoadBalancer{

    private static final Logger logger = LoggerFactory.getLogger(AbstractLoadBalancer.class);

    @Override
    public String getPath(String serviceName) {
        List<ServiceInstance> instances = getInstances(serviceName);

        if (instances.isEmpty()) {
            LoggerUtils.printIfErrorEnabled(logger,"No instances available");
            return null;
        }
        ServiceInstance instance = selectInstance(serviceName, instances);

        return "http://" + instance.getHost() + ":" + instance.getPort();
    }

    /**
     * 根据服务名称获取实例
     */
    protected abstract List<ServiceInstance> getInstances(String serviceName);

    /**
     * 选择实例
     */
    protected abstract ServiceInstance selectInstance(String serviceName,List<ServiceInstance> instances);
}
