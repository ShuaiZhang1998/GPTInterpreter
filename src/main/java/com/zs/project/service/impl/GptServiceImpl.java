package com.zs.project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.service.GptService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.theokanning.openai.service.OpenAiService.*;

/**
 * 需要用流式处理
 */
@Service
@Slf4j
public class GptServiceImpl implements GptService {

    String token = "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";
    String proxyHost = "127.0.0.1";
    int proxyPort = 1111;

    @Override
    public String GptResponse(String query) {
        String keys = "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";
        // 配置代理
        String proxyHost = "127.0.0.1";
        int proxyPort = 1111;
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


    @Override
    @Async
    public void streamChatCompletion(String prompt, SseEmitter sseEmitter) {

        log.info("发送消息：" + prompt);
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), prompt);
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
//                .maxTokens(500)
                .logitBias(new HashMap<>())
                .build();


        //流式对话（逐Token返回）
        StringBuilder receiveMsgBuilder = new StringBuilder();
        OpenAiService service = buildOpenAiService(token, proxyHost, proxyPort);
        service.streamChatCompletion(chatCompletionRequest)
                //正常结束
                .doOnComplete(() -> {
                    log.info("连接结束");

                    //发送连接关闭事件，让客户端主动断开连接避免重连
                    sendStopEvent(sseEmitter);

                    //完成请求处理
                    sseEmitter.complete();
                })
                //异常结束
                .doOnError(throwable -> {
                    log.error("连接异常", throwable);

                    //发送连接关闭事件，让客户端主动断开连接避免重连
                    sendStopEvent(sseEmitter);

                    //完成请求处理携带异常
                    sseEmitter.completeWithError(throwable);
                })
                //收到消息后转发到浏览器
                .blockingForEach(x -> {
                    ChatCompletionChoice choice = x.getChoices().get(0);
                    log.debug("收到消息：" + choice);
                    if (StringUtils.isEmpty(choice.getFinishReason())) {
                        //未结束时才可以发送消息（结束后，先调用doOnComplete然后还会收到一条结束消息，因连接关闭导致发送消息失败:ResponseBodyEmitter has already completed）
                        sseEmitter.send(choice.getMessage());
                    }
                    String content = choice.getMessage().getContent();
                    content = content == null ? StringUtils.EMPTY : content;
                    receiveMsgBuilder.append(content);
                });
        log.info("收到的完整消息：" + receiveMsgBuilder);

    }

    @Override
    public void sendStopEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("stop").data(""));
        } catch (Exception e){
            throw new ServiceException(ErrorCode.SYSTEM_ERROR,"事件停止接口出错");
        }

    }

    @Override
    public OpenAiService buildOpenAiService(String token, String proxyHost, int proxyPort) {
        //构建HTTP代理
        Proxy proxy = null;
        if (StringUtils.isNotBlank(proxyHost)) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        }
        //构建HTTP客户端
        OkHttpClient client = defaultClient(token, Duration.of(60, ChronoUnit.SECONDS))
                .newBuilder()
                .proxy(proxy)
                .build();
        ObjectMapper mapper = defaultObjectMapper();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        OpenAiService service = new OpenAiService(api, client.dispatcher().executorService());
        return service;
    }


}
