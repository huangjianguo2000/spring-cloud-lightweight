package com.huang.lightweight.server.registry.service;

import com.huang.lightweight.common.pojo.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;

import java.util.List;

/**
 * RegistryService
 *
 * @Author lightweight
 * @Date 2023/5/23 11:28
 */
public interface RegistryService {

    /**
     * regitry to server
     *
     * @param instance instance
     * @return true success false fail
     */
    boolean registry(Instance instance);

    /**
     * Returns a list of InstanceWrapper objects containing instances from the cache.
     *
     * @return A list of InstanceWrapper objects
     */
    List<InstanceWrapper> listInstances();
}
