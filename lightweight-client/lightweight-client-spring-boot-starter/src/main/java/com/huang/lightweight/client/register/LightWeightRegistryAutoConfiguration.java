package com.huang.lightweight.client.register;

import org.springframework.context.annotation.Bean;

/**
 * LightWeightRegistryAutoConfiguration
 * Automatic configuration for registering services
 * @author 窝窝头
 * @date 2023/05/24
 */
public class LightWeightRegistryAutoConfiguration {
    /**
     * LightWeightServiceRegister
     * Used to register the current service to
     * @return {@link LightWeightServiceRegister}
     */
    @Bean
    public LightWeightServiceRegister lightWeightServiceRegister(){
        return new LightWeightServiceRegister();
    }
}
