package com.huang.lightweight.server.registry.cluster;

import com.huang.lightweight.common.util.common.StringUtils;

import java.util.Objects;

public class Member implements Comparable<Member>, Cloneable {
    
    private String ip;
    
    private int port = -1;
    
    private volatile NodeState state = NodeState.UP;

    
    private String address = "";

    /**
     * 访问失败
     */
    private transient int failAccessCnt = 0;
    
    public Member() {

    }
    
    public static MemberBuilder builder() {
        return new MemberBuilder();
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public NodeState getState() {
        return state;
    }
    
    public void setState(NodeState state) {
        this.state = state;
    }

    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getAddress() {
        if (StringUtils.isBlank(address)) {
            address = ip + ":" + port;
        }
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public int getFailAccessCnt() {
        return failAccessCnt;
    }
    
    public void setFailAccessCnt(int failAccessCnt) {
        this.failAccessCnt = failAccessCnt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member that = (Member) o;
        if (StringUtils.isAnyBlank(address, that.address)) {
            return port == that.port && StringUtils.equals(ip, that.ip);
        }
        return StringUtils.equals(address, that.address);
    }
    
    @Override
    public String toString() {
        return "Member{" + "ip='" + ip + '\'' + ", port=" + port + ", state=" + state + ", extendInfo="
                + '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
    
    @Override
    public int compareTo(Member o) {
        return getAddress().compareTo(o.getAddress());
    }
    
    public static final class MemberBuilder {
        
        private String ip;
        
        private int port;
        
        private NodeState state;

        
        private MemberBuilder() {
        }
        
        public MemberBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }
        
        public MemberBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public MemberBuilder state(NodeState state) {
            this.state = state;
            return this;
        }

        
        /**
         * build Member.
         *
         * @return {@link Member}
         */
        public Member build() {
            Member serverNode = new Member();
            serverNode.state = this.state;
            serverNode.ip = this.ip;
            serverNode.port = this.port;
            serverNode.address = this.ip + ":" + this.port;
            return serverNode;
        }
    }
    
}