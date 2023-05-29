package com.huang.lightweight.common.common;

import java.util.concurrent.TimeUnit;

/**
 * @Author lightweight
 * @Date 2023/5/24 14:27
 */
public class Constants {

    public static final String CLIENT_VERSION = "1.0.0";

    public static final String NULL = "";

    /**
     * heartbeat detection interval default 15000 ms
     */
    public static final int HEART_BEAT_INTERVAL = 15000;

    /**
     * send heartbeat interval
     */
    public static final int DEFAULT_HEART_BEAT_INTERVAL = 5000;

    /**
     * thread number of beat
     */
    public static final int DEFAULT_CLIENT_BEAT_THREAD_COUNT = Runtime.getRuntime()
            .availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() / 2
            : 1;
}
