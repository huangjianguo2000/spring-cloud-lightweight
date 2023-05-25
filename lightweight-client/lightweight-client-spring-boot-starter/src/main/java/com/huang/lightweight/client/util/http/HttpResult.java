package com.huang.lightweight.client.util.http;

/**
 * HttpResult
 */

public class HttpResult {
    /**
     * response code
     */
    private int code;
    /**
     * response body
     */
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
