package com.example.testconsumer.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author lightweight
 * @Date 2023/5/29 17:18
 */
@Component
public class NameTest {

    @Autowired
    private HelloClient helloClient;

    public void test(){
//        helloTest.sout();
    }
}
