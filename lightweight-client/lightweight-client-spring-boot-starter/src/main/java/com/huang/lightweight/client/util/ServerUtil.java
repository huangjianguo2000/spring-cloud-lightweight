package com.huang.lightweight.client.util;

import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * get server info
 *
 * @author touwowo0320
 * @date 2023/05/25
 */
public class ServerUtil {

    private final static Logger logger = LoggerFactory.getLogger(ServerUtil.class);

    public static String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LoggerUtils.printIfErrorEnabled(logger,"unknown host");
        }
        return null;
    }
}
