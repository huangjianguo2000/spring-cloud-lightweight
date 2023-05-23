package com.huang.lightweight.server.api;

import com.huang.lightweight.server.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/light/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping
    public String hello(){
        return helloService.hello();
    }

}
