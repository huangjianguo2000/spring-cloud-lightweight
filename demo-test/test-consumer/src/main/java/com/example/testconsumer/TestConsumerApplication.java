package com.example.testconsumer;

import com.example.testconsumer.client.HelloTest;
import com.example.testconsumer.client.NameTest;
import com.huang.lightweight.liteconnect.EnableConnectClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConnectClients
public class TestConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }

}
