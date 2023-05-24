package com.huang.lightweight.server.registry.service.impl;

import com.huang.lightweight.common.pojo.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.server.registry.service.RegistryService;
import com.huang.lightweight.server.registry.util.InstanceCachePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * RegistryServiceImpl
 *
 * @Author lightweight
 * @Date 2023/5/23 11:28
 */
@Service
public class RegistryServiceImpl implements RegistryService {

    @Autowired
    private InstanceCachePool instanceCachePool;

    /**
     * regitry to server
     *
     * @param instance instance
     * @return true success false fail
     */
    @Override
    public boolean registry(Instance instance) {
        instanceCachePool.put(instance.getServiceName(), instance);
        return true;
    }

    /**
     * Returns a list of InstanceWrapper objects containing instances from the cache.
     *
     * @return A list of InstanceWrapper objects
     */
    public List<InstanceWrapper> listInstances() {
        List<InstanceWrapper> ans = new ArrayList<>();
        List<List<Instance>> list = instanceCachePool.list();
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

}
