package com.huang.lightweight.server.registry.service.impl;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.server.registry.service.InstanceService;
import com.huang.lightweight.server.registry.util.ScheduledThreadPool;
import com.huang.lightweight.server.registry.util.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RegistryServiceImpl
 *
 * @Author lightweight
 * @Date 2023/5/23 11:28
 */
@Service
public class InstanceServiceImpl implements InstanceService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private ScheduledThreadPool scheduledThreadPool;
    /**
     * regitry to server
     *
     * @param instance instance
     * @return true success false fail
     */
    @Override
    public void registerInstance(Instance instance) throws LightweightException {
        instance.setInstanceId(UUID.randomUUID().toString());
        serviceManager.put(instance.getServiceName(), instance);
        // 心跳检查
        scheduledThreadPool.execute(instance, serviceManager);
    }

    /**
     * update to server
     *
     * @param instance instance
     */
    @Override
    public void updateInstance(Instance instance) throws Exception {
        serviceManager.update(instance.getServiceName(), instance);
    }

    /**
     * Returns a list of InstanceWrapper objects containing instances from the cache.
     *
     * @return A list of InstanceWrapper objects
     */
    public List<InstanceWrapper> listInstances() {
        List<InstanceWrapper> ans = new ArrayList<>();
        List<List<Instance>> list = serviceManager.list();
        // wrapper
        for (List<Instance> instances : list) {
            if (instances != null && !instances.isEmpty()) {
                InstanceWrapper instanceWrapper = new InstanceWrapper();
                instanceWrapper.setServiceName(instances.get(0).getServiceName());
                instanceWrapper.setHosts(instances);
                ans.add(instanceWrapper);
            }
        }
        return ans;
    }

    @Override
    public void beat(Instance instance) throws LightweightException {
        instance.setLastBeat(System.currentTimeMillis());
        //LoggerUtils.printIfDebugEnabled(logger, instance.getIp() + ":" + instance.getPort() + " beat complete");
        serviceManager.update(instance.getServiceName(), instance);
    }

}
