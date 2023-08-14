package com.huang.lightweight.server.registry.service;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;

import java.util.List;

/**
 * RegistryService
 *
 * @Author lightweight
 * @Date 2023/5/23 11:28
 */
public interface InstanceService {

    /**
     * regitry to server
     *
     * @param instance instance
     */
    void registerInstance(Instance instance) throws Exception;

    /**
     * update to server
     *
     * @param instance instance
     */
    void updateInstance(Instance instance) throws Exception;

    /**
     * Returns a list of InstanceWrapper objects containing instances from the cache.
     *
     * @return A list of InstanceWrapper objects
     */
    List<InstanceWrapper> listInstances();

    /**
     * 接收心跳检测
     * @param instance
     * @throws LightweightException
     */
    void beat(Instance instance) throws LightweightException;
}
