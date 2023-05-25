package com.huang.demotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoTestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoTestApplication.class, args);
//        HttpTest httpTest = (HttpTest)applicationContext.getBean("httpTest");
//        httpTest.registry();
    }

}
