package com.huang.lightweight.client.util.http;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.common.util.common.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Http RestClient
 *
 * @author touwowo0320
 * @date 2023/05/25
 */
public final class RestClient {

    private final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private static final RestClient INSTANCE = new RestClient();

    private RestClient() {
    }

    /**
     * 获get RestClient instance
     *
     * @return {@link RestClient}
     */
    public static RestClient getInstance() {
        return INSTANCE;
    }

    /**
     * Simple Encapsulated Post Request
     *
     * @param url    url
     * @param header request header
     * @param data   request body
     * @return {@link HttpResult}
     */
    public HttpResult post(String url, Map<String, String> header, Object data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON));
        if (Objects.nonNull(header) && !header.isEmpty()) {
            header.forEach(post::setHeader);
        }
        post.setEntity(new StringEntity(JSON.toJSONString(data), ContentType.APPLICATION_JSON));
        HttpResult httpResult = new HttpResult();
        try {
            HttpResponse execute = httpClient.execute(post);
            httpResult.setCode(execute.getStatusLine().getStatusCode());
            httpResult.setBody(EntityUtils.toString(execute.getEntity()));
        } catch (IOException e) {
            httpResult.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LoggerUtils.printIfErrorEnabled(logger, "exec post request error!");
        }
        return httpResult;
    }

    /**
     * Check if the request path is legal
     *
     * @param url url
     */
    public void checkUrl(String url) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException();
        }
    }
}