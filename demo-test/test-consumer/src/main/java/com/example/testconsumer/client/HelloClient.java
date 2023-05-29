package com.example.testconsumer.client;

import com.huang.lightweight.liteconnect.ConnectClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author lightweight
 * @Date 2023/5/29 14:45
 */
@ConnectClient(name = "test-produce", beanName = "helloClient")
public interface HelloClient {

    @GetMapping("/hello")
    String hello();

}
