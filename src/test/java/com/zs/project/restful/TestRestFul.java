package com.zs.project.restful;

import net.minidev.json.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;



@SpringBootTest
public class TestRestFul {

    @Test
    void testGet(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.google.com";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }

   @Test
   void testPost() {
       RestTemplate restTemplate = new RestTemplate();
       String apiUrl = "http://10.211.55.2:8000/execute";

       // Create a JSONObject and add the required fields
       JSONObject requestBodyJson = new JSONObject();
       requestBodyJson.put("language", "python");
       requestBodyJson.put("code", "print(\"1\")");

       // Add other optional fields as needed
       // requestBodyJson.put("otherField", "otherValue");

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);

       HttpEntity<String> httpEntity = new HttpEntity<>(requestBodyJson.toString(), headers);

       ResponseEntity<String> response = restTemplate.exchange(
               apiUrl,
               HttpMethod.POST,
               httpEntity,
               String.class
       );

       if (response.getStatusCode().is2xxSuccessful()) {
           System.out.println("POST请求成功！");
           System.out.println("响应体内容：" + response.getBody());
       } else {
           System.out.println("POST请求失败！");
           System.out.println("响应状态码：" + response.getStatusCode());
       }
   }

    @Test
    void testProxy() {
        String keys = "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";
        String ID = "org-dzXy1GOOz3NIh516HiHflgFV";

        // 配置代理
        String proxyHost = "127.0.0.1";  // 替换为你的代理服务器地址
        int proxyPort = 8118;  // 替换为你的代理服务器端口

        RequestConfig config = RequestConfig.custom()
                .setProxy(new org.apache.http.HttpHost(proxyHost, proxyPort))
                .build();

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        String url = "https://www.google.com";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }

    @Test
    void testProxy_() {
        String keys = "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";
        String ID = "org-dzXy1GOOz3NIh516HiHflgFV";

        // 配置代理
        String proxyHost = "127.0.0.1";  // 替换为你的代理服务器地址
        int proxyPort = 8118;  // 替换为你的代理服务器端口

        RequestConfig config = RequestConfig.custom()
                .setProxy(new org.apache.http.HttpHost(proxyHost, proxyPort))
                .build();

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        // 设置请求头部
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + keys);
        headers.set("OpenAI-Organization", ID);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String url = "https://api.openai.com/v1/models";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());
    }



}
