package com.huang.lightweight.produce.controller;

import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Registry-Center Controller.
 *
 * @author lightweight
 */
@RestController
@RequestMapping("/produce")
public class HelloController {


    /**
     * Registers a service instance.
     *
     * @param instance The service instance object to be registered.
     * @return The result of the operation.
     */
    @PostMapping("/instance")
    public Result<String> registry(@RequestBody Instance instance){
        return Result.success("1");
    }

    @GetMapping("/instance")
    public Result<List<InstanceWrapper>> listInstance(){
        return Result.success();
    }
    @GetMapping("/hello")
    public Result<String> hello(){
        return Result.success("hello word of 9001");
    }
}
