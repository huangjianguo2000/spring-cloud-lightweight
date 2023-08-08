package com.huang.lightweight.liteconnect.balancer;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContextAware;

public interface LoadBalancer{


    /**
     * 获取远程服务的IP地址和端口的完整地址 http://xxx:port
     * @param serviceName 服务名称
     * @return
     */
    String getPath(String serviceName);

}
