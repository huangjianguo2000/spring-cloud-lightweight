package com.huang.lightweight.client.naming;

import com.huang.lightweight.client.factory.PropertyKeyConst;
import com.huang.lightweight.client.naming.net.NamingProxy;
import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.instance.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class LightweightNamingService implements NamingService {

    private final Logger logger = LoggerFactory.getLogger(LightweightNamingService.class);

    /**
     * network proxy object
     */
    private NamingProxy serverProxy;

    /**
     * server ip list like xxx:xx,xxx:xx
     */
    private String serverList;

    public LightweightNamingService(Properties properties) {
        init(properties);
    }

    /**
     * Registers an instance with the naming service.
     *
     * @param serviceName The name of the service.
     * @param instance    The instance object to be registered.
     */
    @Override
    public void registerInstance(String serviceName, Instance instance) {
        serverProxy.registerService(serviceName, instance);
    }

    /**
     * Retrieves all instances associated with the specified service name.
     *
     * @param serviceName The name of the service.
     * @return A list of instances.
     * @throws LightweightException A lightweight system exception that may be thrown while retrieving the instance list.
     */
    @Override
    public List<Instance> getAllInstances(String serviceName) throws LightweightException {
        return null;
    }

    /**
     * Initializes the LightweightNamingService with the given properties.
     *
     * @param properties The properties to initialize the service.
     */
    public void init(Properties properties) {
        initServerAddress(properties);
        serverProxy = new NamingProxy(serverList, properties);
    }

    /**
     * Initializes the server address from the provided properties.
     *
     * @param properties The properties containing the server address.
     */
    private void initServerAddress(Properties properties) {
        serverList = properties.getProperty(PropertyKeyConst.SERVER_ADDRESS);
    }

}
