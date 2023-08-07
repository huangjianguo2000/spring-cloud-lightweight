package com.huang.lightweight.client.naming;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.instance.Instance;

import java.util.List;

public interface NamingService {

    /**
     * Registers an instance with the naming service.
     *
     * @param serviceName The name of the service.
     * @param instance    The instance object to be registered.
     */
    void registerInstance(String serviceName, Instance instance);

    /**
     * Retrieves all instances associated with the specified service name.
     *
     * @param serviceName The name of the service.
     * @return A list of instances.
     * @throws LightweightException A lightweight system exception that may be thrown while retrieving the instance list.
     */
    List<Instance> getAllInstances(String serviceName) throws LightweightException;

}

