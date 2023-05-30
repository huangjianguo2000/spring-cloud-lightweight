package com.huang.lightweight.client.discovery;

import com.huang.lightweight.client.LightweightDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lightweight
 * @Date 2023/5/29 20:45
 */

@Configuration
@ConditionalOnDiscoveryEnabled
public class LightweightDiscoveryAutoConfiguration {

    public LightweightDiscoveryAutoConfiguration() {
        // do nothing
    }

    @Bean
    @ConditionalOnMissingBean
    public LightweightDiscoveryProperties lightweightDiscoveryProperties() {
        return new LightweightDiscoveryProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public LightweightServiceDiscovery lightweightServiceDiscovery(LightweightDiscoveryProperties lightweightDiscoveryProperties) {
        return new LightweightServiceDiscovery(lightweightDiscoveryProperties);
    }

}
