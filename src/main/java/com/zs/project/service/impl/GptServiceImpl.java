package com.zs.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.service.GptService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 需要用流式处理
 */
@Service
public class GptServiceImpl implements GptService {
    @Override
    public String GptResponse(String query) {
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
        // 修改content
        String content = query;
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);

        requestBody.put("messages", Collections.singletonList(message));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try{
            rootNode = objectMapper.readTree(response.getBody());
        }
        catch (JsonProcessingException e){
            throw new ServiceException(ErrorCode.SYSTEM_ERROR,"json失败");
        }

        JsonNode choicesNode = rootNode.path("choices");
        JsonNode firstChoiceNode = choicesNode.get(0);
        JsonNode messageNode = firstChoiceNode.path("message");
        String gptResponse = messageNode.path("content").asText();
        return gptResponse;
    }
}
