package com.huang.consumer.controller;

import com.huang.consumer.client.HelloClient;
import com.huang.lightweight.common.model.v1.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lightweight
 * @Date 2023/5/29 11:05
 */
@RestController
@RequestMapping("/produce")
public class HelloController {

    @Autowired
    private HelloClient helloClient;

    @GetMapping("/hello")
    public Result<String> hello(){
        Result<String> hello = helloClient.hello();
        System.out.println(hello);
        return Result.success(hello.getData());
    }

}
