package com.example.testproduce.controller;

import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.pojo.instance.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Registry-Center Controller.
 *
 * @author lightweight
 */
@RestController
public class HelloController {

    @Value("${server.port}")
    private Integer port;
    @GetMapping("/hello")
    public Result<String> hello(){
        return Result.success("hello word of produce port = " + port);
    }


    @GetMapping("/hello2")
    public Result<String> hello2(){
        return Result.success("hello word of produce");
    }
}
