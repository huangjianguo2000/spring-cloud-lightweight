package com.huang.lightweight.common.common;

/**
 * @Author lightweight
 * @Date 2023/5/24 14:27
 */
public class Constants {

    public static final String NAME = "/light";

    public static final String LIGHTWEIGHT_SERVER_VERSION = NAME + "/v1";

    public static final String DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT = LIGHTWEIGHT_SERVER_VERSION + "/rc";

    public static final String NULL = "";

    public static final String NET_PROTOCOL = "http://";

    /**
     * 心跳检测过期时间
     */
    public static final int HEART_BEAT_INTERVAL = 15000;

    /**
     * 发送心跳检测间隔
     */
    public static final int DEFAULT_HEART_BEAT_INTERVAL = 5000;

    /**
     * 心跳检测线程池线程数量
     */
    public static final int DEFAULT_CLIENT_BEAT_THREAD_COUNT = Runtime.getRuntime()
            .availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() / 2
            : 1;
}
