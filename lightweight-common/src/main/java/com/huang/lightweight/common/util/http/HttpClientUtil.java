package com.huang.lightweight.common.util.http;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.common.util.common.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Http RestClient
 *
 * @author touwowo0320
 * @date 2023/05/25
 */
public final class HttpClientUtil {

    private final Logger logger = new Logger() {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean isTraceEnabled() {
            return false;
        }

        @Override
        public void trace(String s) {

        }

        @Override
        public void trace(String s, Object o) {

        }

        @Override
        public void trace(String s, Object o, Object o1) {

        }

        @Override
        public void trace(String s, Object... objects) {

        }

        @Override
        public void trace(String s, Throwable throwable) {

        }

        @Override
        public boolean isTraceEnabled(Marker marker) {
            return false;
        }

        @Override
        public void trace(Marker marker, String s) {

        }

        @Override
        public void trace(Marker marker, String s, Object o) {

        }

        @Override
        public void trace(Marker marker, String s, Object o, Object o1) {

        }

        @Override
        public void trace(Marker marker, String s, Object... objects) {

        }

        @Override
        public void trace(Marker marker, String s, Throwable throwable) {

        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void debug(String s) {

        }

        @Override
        public void debug(String s, Object o) {

        }

        @Override
        public void debug(String s, Object o, Object o1) {

        }

        @Override
        public void debug(String s, Object... objects) {

        }

        @Override
        public void debug(String s, Throwable throwable) {

        }

        @Override
        public boolean isDebugEnabled(Marker marker) {
            return false;
        }

        @Override
        public void debug(Marker marker, String s) {

        }

        @Override
        public void debug(Marker marker, String s, Object o) {

        }

        @Override
        public void debug(Marker marker, String s, Object o, Object o1) {

        }

        @Override
        public void debug(Marker marker, String s, Object... objects) {

        }

        @Override
        public void debug(Marker marker, String s, Throwable throwable) {

        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public void info(String s) {

        }

        @Override
        public void info(String s, Object o) {

        }

        @Override
        public void info(String s, Object o, Object o1) {

        }

        @Override
        public void info(String s, Object... objects) {

        }

        @Override
        public void info(String s, Throwable throwable) {

        }

        @Override
        public boolean isInfoEnabled(Marker marker) {
            return false;
        }

        @Override
        public void info(Marker marker, String s) {

        }

        @Override
        public void info(Marker marker, String s, Object o) {

        }

        @Override
        public void info(Marker marker, String s, Object o, Object o1) {

        }

        @Override
        public void info(Marker marker, String s, Object... objects) {

        }

        @Override
        public void info(Marker marker, String s, Throwable throwable) {

        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public void warn(String s) {

        }

        @Override
        public void warn(String s, Object o) {

        }

        @Override
        public void warn(String s, Object... objects) {

        }

        @Override
        public void warn(String s, Object o, Object o1) {

        }

        @Override
        public void warn(String s, Throwable throwable) {

        }

        @Override
        public boolean isWarnEnabled(Marker marker) {
            return false;
        }

        @Override
        public void warn(Marker marker, String s) {

        }

        @Override
        public void warn(Marker marker, String s, Object o) {

        }

        @Override
        public void warn(Marker marker, String s, Object o, Object o1) {

        }

        @Override
        public void warn(Marker marker, String s, Object... objects) {

        }

        @Override
        public void warn(Marker marker, String s, Throwable throwable) {

        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public void error(String s) {

        }

        @Override
        public void error(String s, Object o) {

        }

        @Override
        public void error(String s, Object o, Object o1) {

        }

        @Override
        public void error(String s, Object... objects) {

        }

        @Override
        public void error(String s, Throwable throwable) {

        }

        @Override
        public boolean isErrorEnabled(Marker marker) {
            return false;
        }

        @Override
        public void error(Marker marker, String s) {

        }

        @Override
        public void error(Marker marker, String s, Object o) {

        }

        @Override
        public void error(Marker marker, String s, Object o, Object o1) {

        }

        @Override
        public void error(Marker marker, String s, Object... objects) {

        }

        @Override
        public void error(Marker marker, String s, Throwable throwable) {

        }
    };

    private static final HttpClientUtil INSTANCE = new HttpClientUtil();

    private HttpClientUtil() {
    }

    /**
     * 获get RestClient instance
     *
     * @return {@link HttpClientUtil}
     */
    public static HttpClientUtil getInstance() {
        return INSTANCE;
    }

    public HttpResult post(String url, Object data) {
        return post(url, null, data);
    }

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

    public HttpResult get(String url) {
        return this.get(url, new HashMap<>(), new HashMap<>());
    }

    public HttpResult get(String url, Map<String, String> params) {
        return this.get(url, new HashMap<>(), params);
    }

    public HttpResult get(String url, Map<String, String> header, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Build the query string with parameters
        StringBuilder query = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (query.length() > 0) {
                    query.append("&");
                }
                try {
                    query.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), String.valueOf(StandardCharsets.UTF_8)));
                } catch (Exception e) {
                    LoggerUtils.printIfErrorEnabled(logger, "query append error : e = {}", e);
                }
            }
            url += "?" + query;
        }

        HttpGet get = new HttpGet(url);
        if (Objects.nonNull(header) && !header.isEmpty()) {
            header.forEach(get::setHeader);
        }

        HttpResult httpResult = new HttpResult();
        HttpResponse response = null;

        try {
            LoggerUtils.printIfInfoEnabled(logger, "send get url = {}", url);
            response = httpClient.execute(get);
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setBody(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            httpResult.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LoggerUtils.printIfErrorEnabled(logger, "exec get request error! url={}, httpResult = {}, response = {}",url, httpResult, response);
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