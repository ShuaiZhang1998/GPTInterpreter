package com.zs.project.model.dto.gpt;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GPT4
 * 用建造者模式来构建请求的header
 */
public class OpenAIRequestBuilder {
    /**
     * 模型请求地址
     */
    public static final String OPENAI_ENDPOINT_URL = "https://api.openai.com/v1/chat/completions";

    private final Map<String, Object> requestBody = new HashMap<>();
    private final Map<String, Object> message = new HashMap<>();

    private final String token;
    
    public OpenAIRequestBuilder(String token) {
        // 设置默认的参数
        this.token = token;
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", 0.7);
    }
    
    public OpenAIRequestBuilder withContent(String content) {
        message.put("role", "user");
        message.put("content", content);
        requestBody.put("messages", Collections.singletonList(message));
        return this;
    }

    public Map<String, Object> buildRequestBody() {
        return requestBody;
    }

    public String getUrl() {
        return OPENAI_ENDPOINT_URL;
    }

    public HttpEntity<Map<String, Object>> buildRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(buildRequestBody(), headers);
    }


}
