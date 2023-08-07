package com.huang.lightweight.client.discovery;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lightweight
 * @Date 2023/5/29 20:44
 */

@Configuration
@AutoConfigureBefore({SimpleDiscoveryClientAutoConfiguration.class, CommonsClientAutoConfiguration.class})
@AutoConfigureAfter({LightweightDiscoveryAutoConfiguration.class})
public class LightweightDiscoveryClientConfiguration {

    @Bean
    public LightweightDiscoveryClient lightweightDiscoveryClient(LightweightServiceDiscovery lightweightServiceDiscovery){
        return new LightweightDiscoveryClient(lightweightServiceDiscovery);
    }

}
