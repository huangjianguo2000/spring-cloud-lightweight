package com.huang.consumer.client;

import com.huang.lightweight.common.model.v1.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nacos-payment-provider")
public interface HelloClient {

    @GetMapping("/produce/hello")
    Result<String> hello();
}
