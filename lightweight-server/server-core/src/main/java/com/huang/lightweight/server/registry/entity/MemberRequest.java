package com.huang.lightweight.server.registry.entity;

import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.server.registry.cluster.Member;

import java.util.List;

/**
 * @Author lightweight
 * @Date 2023/8/13 20:50
 */
public class MemberRequest {

    private Member member;

    private List<InstanceWrapper> instances;

    public MemberRequest(Member member, List<InstanceWrapper> instances) {
        this.member = member;
        this.instances = instances;
    }

    public MemberRequest() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<InstanceWrapper> getInstances() {
        return instances;
    }

    public void setInstances(List<InstanceWrapper> instances) {
        this.instances = instances;
    }
}
