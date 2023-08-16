package com.zs.project.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestChatGPT {

    @Test
    void testProxy() throws JsonProcessingException {
        String keys = "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";

        // 配置代理
        String proxyHost = "127.0.0.1";
        int proxyPort = 8118;

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
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + keys);

        // 创建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", 0.7);

        // 如果你想要更改content，只需要更改此处的内容即可
        String content = "你好啊！";
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);

        requestBody.put("messages", Collections.singletonList(message));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode choicesNode = rootNode.path("choices");
        JsonNode firstChoiceNode = choicesNode.get(0);
        JsonNode messageNode = firstChoiceNode.path("message");
        String content_ = messageNode.path("content").asText();



    }
}
