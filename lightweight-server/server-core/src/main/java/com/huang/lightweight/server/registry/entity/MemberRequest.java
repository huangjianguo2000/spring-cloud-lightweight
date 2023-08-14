package com.huang.lightweight.server.registry.entity;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.server.registry.cluster.Member;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public MemberRequest(String data) {
        MemberRequest memberRequest = JSON.parseObject(data, MemberRequest.class);
        this.instances = memberRequest.getInstances();
        this.member = memberRequest.getMember();
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

    public Map<String, List<Instance>> convertToMap(){
        ConcurrentHashMap<String, List<Instance>> ans = new ConcurrentHashMap<>();
        for (InstanceWrapper instanceWrapper : instances) {
            List<Instance> hosts = instanceWrapper.getHosts();
            ans.put(instanceWrapper.getServiceName(), hosts);
        }
        return ans;
    }
}
