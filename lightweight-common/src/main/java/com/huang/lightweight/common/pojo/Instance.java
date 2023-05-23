package com.huang.lightweight.common.pojo;

import java.io.Serializable;

/**
 * Instance.
 * @author lightweight
 */
public class Instance implements Serializable {

    private static final long serialVersionUID = -742906310567291979L;

    /**
     * unique id of this instance.
     */
    private String instanceId;

    /**
     * instance ip.
     */
    private String ip;

    /**
     * instance port.
     */
    private int port;

    /**
     * instance health status.
     */
    private boolean healthy = true;

    /**
     * If instance is enabled to accept request.
     */
    private boolean enabled = true;

    /**
     * Service information of instance.
     */
    private String serviceName;


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
