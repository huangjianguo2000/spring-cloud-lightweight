package com.huang.lightweight.server.registry.cluster;

public enum NodeState {
    
    /**
     * Node is starting.
     */
    STARTING,
    
    /**
     * Node is up and ready for request.
     */
    UP,
    
    /**
     * Node may Crash.节点可能崩溃了
     */
    SUSPICIOUS,
    
    /**
     * Node is out of service, something abnormal happened.节点停止服务，出现异常。
     */
    DOWN,
    
    /**
     * The Node is isolated. 节点处于隔离状态。
     */
    ISOLATION,
    
}
