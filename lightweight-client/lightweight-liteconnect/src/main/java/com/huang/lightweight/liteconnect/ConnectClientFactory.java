package com.huang.lightweight.liteconnect;

import com.huang.lightweight.common.util.http.HttpClientUtil;
import com.huang.lightweight.common.util.http.HttpResult;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class ConnectClientFactory implements FactoryBean<Object> {

    private final Class<?> interfaceClass;
    private String name; // 添加 name 属性
    public ConnectClientFactory(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ConnectClientInvocationHandler()
        );
    }


    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private class ConnectClientInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 在此处实现自定义的远程调用逻辑
            String url = ((ConnectClient) interfaceClass.getAnnotation(ConnectClient.class)).name();
            // 调用远程服务，返回结果
            // ...
            HttpResult httpResult = HttpClientUtil.getInstance().get("http://localhost:8001/hello");
            return httpResult.getBody();
        }
    }

}
