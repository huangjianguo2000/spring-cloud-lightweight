package com.huang.lightweight.client.registry;

import com.huang.lightweight.client.LightweightDiscoveryProperties;
import com.huang.lightweight.client.naming.NamingService;
import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * LightWeightServiceRegister
 *
 * @author 窝窝头
 * @date 2023/05/24
 */
public class LightweightServiceRegistry implements ServiceRegistry<Registration> {

    private final Logger logger = LoggerFactory.getLogger(LightweightServiceRegistry.class);
    
    private final NamingService namingService;
    
    public LightweightServiceRegistry(LightweightDiscoveryProperties lightweightDiscoveryProperties) {
        this.namingService = lightweightDiscoveryProperties.getNamingService();
    }

    /**
     * Obtain relevant configurations from the running environment for registration services
     */
    @Override
    public void register(Registration registration) {
        if (StringUtils.isEmpty(registration.getServiceId())) {
            LoggerUtils.printIfWarnEnabled(logger, "No service to register for lightweight client...");
        } else {
            String serviceId = registration.getServiceId();
            Instance instance = this.getLightweightInstanceFromRegistration(registration);

            try {
                namingService.registerInstance(serviceId, instance);
                LoggerUtils.printIfInfoEnabled(logger, "lightweight registry,  {} {}:{} register finished", serviceId, instance.getIp(), instance.getPort());
            } catch (Exception e) {
                LoggerUtils.printIfErrorEnabled(logger, "lightweight registry, {} register failed...{}", serviceId, e);
                rethrowRuntimeException(e);
            }
        }
    }

    @Override
    public void deregister(Registration registration) {

    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(Registration registration, String status) {

    }

    @Override
    public Object getStatus(Registration registration) {
        return registration;
    }

    private Instance getLightweightInstanceFromRegistration(Registration registration) {
        Instance instance = new Instance();
        instance.setIp(registration.getHost());
        instance.setPort(registration.getPort());
        return instance;
    }
}
