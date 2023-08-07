package com.huang.lightweight.server.bootstrap.controller;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.util.common.LoggerUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class IndexController {

    private static String address = null;

    private static final AtomicBoolean finished = new AtomicBoolean(false);


    private final ResourceLoader resourceLoader;

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    public IndexController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/")
    public String hello(HttpServletRequest httpServletRequest) throws LightweightException {
//        if (address == null) {
//            if (!finished.get()) {
//                setIpAddress(httpServletRequest);
//            }
//        }
        return "/index";
    }

//
//    private synchronized void setIpAddress(HttpServletRequest httpServletRequest) throws LightweightException {
//        if (finished.get()) {
//            return;
//        }
//
//        try {
//            Resource resource = resourceLoader.getResource("classpath:/templates/assets/service.dbd65b9c.js");
//            String content = readResourceContent(resource);
//            String modifiedContent = modifyContent(content, httpServletRequest.getRequestURL().toString());
//            writeResourceContent(resource.getFile(), modifiedContent);
//        } catch (Exception e) {
//            LoggerUtils.printIfErrorEnabled(logger, "set web ip exception = {}", e);
//            throw new LightweightException(ErrorCode.SERVER_SET_WEB_IP_EXCEPTION, e);
//        }
//
//        finished.compareAndSet(false, true);
//        LoggerUtils.printIfInfoEnabled(logger, "set web Ip success");
//    }
//
//    private String readResourceContent(Resource resource) throws IOException {
//        try (InputStream inputStream = resource.getInputStream()) {
//            return new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
//        }
//    }
//
//    private String modifyContent(String content, String newAddress) {
//        newAddress = removeTrailingSlash(newAddress);
//        return content.replace("http://localhost:5013", newAddress);
//    }
//
//    private String removeTrailingSlash(String address) {
//        if (address.charAt(address.length() - 1) == '/') {
//            return address.substring(0, address.length() - 1);
//        }
//        return address;
//    }
//
//    private void writeResourceContent(File file, String content) throws IOException {
//        try (FileWriter writer = new FileWriter(file)) {
//            writer.write(content);
//        }
//    }
}
