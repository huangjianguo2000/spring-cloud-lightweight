package com.huang.demotest.test;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.pojo.instance.Instance;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @Author lightweight
 * @Date 2023/5/23 16:44
 */
@Component("httpTest")
public class HttpTest implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    public void registry(){

        String url = "http://10.33.36.169:8527/light/v1/rc/instance";
        HttpPost post = new HttpPost(url);
        Instance instance = new Instance();
        instance.setInstanceId(UUID.randomUUID().toString());
        instance.setIp(getIpAddress());
        instance.setPort(serverPort);
        instance.setServiceName("produce-service");
        //创建httpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        // 设置请求头的Content-Type为application/json
        post.setHeader("Content-Type", "application/json");
        try {
            System.out.println(JSON.toJSONString(instance));
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(instance),  ContentType.APPLICATION_JSON);
            //设置请求参数
            post.setEntity(stringEntity);
            System.out.println(stringEntity);
            //调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse/CloseableHttpResponse。
            HttpResponse execute = client.execute(post);
            System.out.println(execute);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        this.serverPort = webServerInitializedEvent.getWebServer().getPort();
    }

    public String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
