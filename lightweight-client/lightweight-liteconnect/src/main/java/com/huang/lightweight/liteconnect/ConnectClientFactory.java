package com.huang.lightweight.liteconnect;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.util.http.HttpClientUtil;
import com.huang.lightweight.common.util.http.HttpResult;
import com.huang.lightweight.liteconnect.balancer.DefaultLoadBalancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ConnectClientFactory implements FactoryBean<Object>, ApplicationContextAware {

    private final Class<?> interfaceClass;

    private DefaultLoadBalancer defaultLoadBalancer;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
         defaultLoadBalancer = new DefaultLoadBalancer(applicationContext);
    }

    private class ConnectClientInvocationHandler implements InvocationHandler {
        @Override
        public String invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 服务名称
            String serviceName = interfaceClass.getAnnotation(ConnectClient.class).name();

            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                String[] value = getMapping.value();
                if (value.length > 0) {
                    String url =defaultLoadBalancer.getPath(serviceName) + value[0];
                    try {
                        HttpResult httpResult = HttpClientUtil.getInstance().get(url);
                        if (httpResult.getBody() != null) {
                            return httpResult.getBody();
                        } else {
                            // Handle null response or body
                            return "Empty response or body";
                        }
                    } catch (Exception e) {
                        // Handle exception
                        return "Error occurred: " + e.getMessage();
                    }
                } else {
                    // Handle empty value
                    return "Empty URL value";
                }
            }else{
                throw new LightweightException(ErrorCode.CONNECT_TYPE_ERROR, "Currently, only GET requests are supported");
            }

           // return "error";
        }
        @Override
        public boolean equals(Object obj) {
            // Your equals() implementation logic
            return true;
        }

    }

}
