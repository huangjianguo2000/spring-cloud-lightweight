package com.huang.lightweight.client.registry;

import com.huang.lightweight.client.LightweightDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(
        value = {"spring.cloud.service-registry.auto-registration.enabled"},
        matchIfMissing = true
)
@EnableConfigurationProperties({LightweightDiscoveryProperties.class,AutoServiceRegistrationProperties.class})
public class LightweightServiceRegistryAutoConfiguration {


    @Bean
    public LightweightServiceRegistry lightweightServiceRegistry(LightweightDiscoveryProperties lightweightDiscoveryProperties) {
        return new LightweightServiceRegistry(lightweightDiscoveryProperties);
    }

    @Bean
    public LightweightRegistration lightweightRegistration(LightweightDiscoveryProperties lightweightDiscoveryProperties) {
        return new LightweightRegistration(lightweightDiscoveryProperties);
    }

    @Bean
    public LightweightAutoServiceRegistration lightweightAutoServiceRegistration(LightweightServiceRegistry lightweightServiceRegistry, LightweightRegistration lightweightRegistration) {
        return new LightweightAutoServiceRegistration(lightweightServiceRegistry, lightweightRegistration, null);
    }

}
