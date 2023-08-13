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
     * Node may Crash.
     */
    SUSPICIOUS,
    
    /**
     * Node is out of service, something abnormal happened.
     */
    DOWN,
    
    /**
     * The Node is isolated.
     */
    ISOLATION,
    
}
