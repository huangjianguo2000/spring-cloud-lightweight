package com.huang.lightweight.common.pojo.instance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Instance.
 *
 * @author lightweight
 */
public class Instance implements Serializable {

    private static final long serialVersionUID = -742906310567291979L;

    /**
     * 实例的ID
     */
    private String instanceId;

    /**
     * 实例的IP地址
     */
    private String ip;

    /**
     * 实例的端口
     */
    private int port;

    /**
     * 当前实例是否健康.
     */
    private boolean healthy = true;

    /**
     * If instance is enabled to accept request.
     */
    private boolean enabled = true;

    /**
     * 服务名称.
     */
    private String serviceName;

    /**
     * 权重-> 负载均衡时候使用
     */
    private Integer weight;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 元数据
     */
    private Map<String, String> metadata = new HashMap();

    /**
     * 上一次心跳检测时间
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


    public boolean equals(Instance instance) {
        return this.ip.equals(instance.ip) && this.port == instance.port;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "instanceId='" + instanceId + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", healthy=" + healthy +
                ", enabled=" + enabled +
                ", serviceName='" + serviceName + '\'' +
                ", weight=" + weight +
                ", clusterName='" + clusterName + '\'' +
                ", metadata=" + metadata +
                ", lastBeat=" + lastBeat +
                '}';
    }

}
