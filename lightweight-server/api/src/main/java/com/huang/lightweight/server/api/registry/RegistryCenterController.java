package com.huang.lightweight.server.api.registry;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/light/v1/rc")
public class RegistryCenterController {


    @PostMapping("/instance")
    public String registry(){

        return "注册成功";
    }

}
