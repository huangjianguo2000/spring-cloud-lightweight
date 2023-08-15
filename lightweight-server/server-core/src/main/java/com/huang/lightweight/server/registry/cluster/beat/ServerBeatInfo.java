package com.huang.lightweight.server.registry.cluster.beat;

import com.huang.lightweight.server.registry.entity.MemberRequest;
import com.huang.lightweight.server.registry.ServiceManager;

/**
 * @Author lightweight
 * @Date 2023/8/13 20:17
 */
public class ServerBeatInfo {

    private MemberRequest memberRequest;

    private long period;

    private ServiceManager serviceManager;

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public MemberRequest getMemberRequest() {
        return memberRequest;
    }

    public void setMemberRequest(MemberRequest memberRequest) {
        this.memberRequest = memberRequest;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
