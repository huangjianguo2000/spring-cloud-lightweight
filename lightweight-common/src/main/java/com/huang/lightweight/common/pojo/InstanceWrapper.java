package com.huang.lightweight.common.pojo;

import com.huang.lightweight.common.pojo.instance.Instance;

import java.util.List;

/**
 * @Author lightweight
 * @Date 2023/5/23 14:34
 */
public class InstanceWrapper {

    /**
     * service-name
     */
    private String serviceName;

    /**
     * the list of instances
     */
    private List<Instance> hosts;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Instance> getHosts() {
        return hosts;
    }

    public void setHosts(List<Instance> hosts) {
        this.hosts = hosts;
    }
}
