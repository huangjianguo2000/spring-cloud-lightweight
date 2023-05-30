package com.huang.lightweight.liteconnect;

import com.huang.lightweight.common.util.http.HttpClientUtil;
import com.huang.lightweight.common.util.http.HttpResult;
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

    private ApplicationContext applicationContext;

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
        this.applicationContext = applicationContext;
    }

    private class ConnectClientInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            String serviceName = ((ConnectClient) interfaceClass.getAnnotation(ConnectClient.class)).name();
            DiscoveryClient discoveryClient = applicationContext.getBean(DiscoveryClient.class);
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

            // get Annotation GetMapping
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                String[] value = getMapping.value();
                if (value != null && value.length > 0 && "/hello".equals(value[0])) {
                    String url = "http://" + instances.get(0).getHost() + ":" + instances.get(0).getPort();
                    HttpResult httpResult = HttpClientUtil.getInstance().get(url + value[0]);
                    return httpResult.getBody();
                }
            }

            return "error";
        }
    }

}
