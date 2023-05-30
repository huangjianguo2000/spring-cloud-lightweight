package com.huang.lightweight.client.naming;

import com.huang.lightweight.client.factory.PropertyKeyConst;
import com.huang.lightweight.client.naming.beat.BeatInfo;
import com.huang.lightweight.client.naming.beat.BeatReactor;
import com.huang.lightweight.client.naming.net.NamingProxy;
import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.instance.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class LightweightNamingService implements NamingService {

    private final Logger logger = LoggerFactory.getLogger(LightweightNamingService.class);


    private NamingProxy serverProxy;

    private BeatReactor beatReactor;

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

        BeatInfo beatInfo = new BeatInfo();
        beatInfo.setServiceName(serviceName);
        beatInfo.setIp(instance.getIp());
        beatInfo.setPort(instance.getPort());
        beatInfo.setPeriod(Constants.DEFAULT_HEART_BEAT_INTERVAL);

        beatReactor.addBeatInfo(beatInfo);
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
        Map<String, List<Instance>> stringListMap = serverProxy.listInstance();
        return stringListMap.get(serviceName);
    }

    /**
     * Initializes the LightweightNamingService with the given properties.
     *
     * @param properties The properties to initialize the service.
     */
    public void init(Properties properties) {
        initServerAddress(properties);
        serverProxy = new NamingProxy(serverList, properties);
        beatReactor = new BeatReactor(serverProxy, Constants.DEFAULT_CLIENT_BEAT_THREAD_COUNT);
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
