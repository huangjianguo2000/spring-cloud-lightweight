package com.huang.lightweight.server.bootstrap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        spring.mvc.view.prefix + /index + spring.mvc.view.suffix 构成完整的请求地址
//        访问项目根目录即是访问 /views/index.html页面
        registry.addViewController("/").setViewName("/index");
    }
    
}
