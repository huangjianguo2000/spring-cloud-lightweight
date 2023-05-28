package com.huang.lightweight.client.naming.beat;


public class BeatInfo {

    private int port;

    private String ip;

    private String serviceName;

    private volatile boolean scheduled;

    private volatile long period;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "BeatInfo{" +
                "port=" + port +
                ", ip='" + ip + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", scheduled=" + scheduled +
                ", period=" + period +
                '}';
    }
}
