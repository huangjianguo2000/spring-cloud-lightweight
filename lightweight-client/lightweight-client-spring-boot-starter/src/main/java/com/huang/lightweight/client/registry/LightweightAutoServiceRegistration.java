package com.huang.lightweight.client.registry;

import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;

/**
 * LightweightAutoServiceRegistration
 *
 * @Author lightweight
 * @Date 2023/5/23 14:34
 */
public class LightweightAutoServiceRegistration extends AbstractAutoServiceRegistration<Registration> {

    private final Logger logger = LoggerFactory.getLogger(LightweightAutoServiceRegistration.class);

    /**
     * Information about the service
     */
    private final LightweightRegistration lightweightRegistration;

    public LightweightAutoServiceRegistration(LightweightServiceRegistry serviceRegistry, LightweightRegistration lightweightRegistration, AutoServiceRegistrationProperties properties) {
        super(serviceRegistry, properties);
        this.lightweightRegistration = lightweightRegistration;
    }

    @Override
    protected Object getConfiguration() {
        return null;
    }

    /**
     * Checks if auto service registration is enabled.
     *
     * @return true if auto service registration is enabled, false otherwise.
     */
    @Override
    protected boolean isEnabled() {
        return lightweightRegistration.getLightWeightDiscoveryProperties().isRegisterEnabled();
    }

    /**
     * Retrieves the registration information for the service.
     *
     * @return The registration object containing service information.
     */
    @Override
    protected Registration getRegistration() {
        if (lightweightRegistration.getPort() == -1) {
            // Set the port if not already set
            lightweightRegistration.setPort(this.getPort().get());
        }
        return lightweightRegistration;
    }

    /**
     * Registers the service with the Lightweight Service Registry.
     */
    @Override
    protected void register() {
        if (!this.lightweightRegistration.getLightWeightDiscoveryProperties().isRegisterEnabled()) {
            LoggerUtils.printIfWarnEnabled(logger, "Registration disabled.");
        } else {
            if (this.lightweightRegistration.getPort() < 0) {
                // Set the port if not already set
                this.lightweightRegistration.setPort(this.getPort().get());
            }
            super.register();
        }
    }

    @Override
    protected Registration getManagementRegistration() {
        return null;
    }
}
