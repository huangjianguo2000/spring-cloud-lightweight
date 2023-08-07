package com.example.testconsumer;

import com.example.testconsumer.client.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lightweight
 * @Date 2023/5/29 19:34
 */
@RestController
public class HelloController {

    @Autowired
    private HelloClient helloClient;

    @GetMapping("/hello")
    public String hello(){
        System.out.println("testsss");
        return helloClient.hello();
    }

}
