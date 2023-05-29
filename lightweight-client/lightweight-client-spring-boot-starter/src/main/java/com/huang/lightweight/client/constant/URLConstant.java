package com.huang.lightweight.client.constant;

import java.util.concurrent.TimeUnit;

/**
 * Util and constants.
 * used for storage light-weight server interfaces
 * @author 窝窝头
 * @date 2023/05/24
 */
public class URLConstant {
    /**
     * webContext, base request url.
     */
    public static String webContext = "/light";

    /**
     * instanceUrl, instance request url, for register and discovery.
     */
    public static String instanceUrl = webContext + "/v1/rc/instance";

    public static String SEND_BEAT = instanceUrl + "/beat";

}
