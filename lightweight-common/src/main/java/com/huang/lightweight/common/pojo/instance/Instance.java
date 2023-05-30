package com.huang.lightweight.common.pojo.instance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * weight
     */
    private Integer weight;

    /**
     * clusterName
     */
    private String clusterName;

    /**
     * metadata
     */
    private Map<String, String> metadata = new HashMap();

    /**
     * last beat time
     */
    private volatile long lastBeat = System.currentTimeMillis();

    public long getLastBeat() {
        return lastBeat;
    }

    public void setLastBeat(long lastBeat) {
        this.lastBeat = lastBeat;
    }

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
