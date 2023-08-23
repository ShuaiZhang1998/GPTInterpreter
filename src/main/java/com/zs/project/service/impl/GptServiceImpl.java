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
import com.zs.project.model.dto.gpt.OpenAIRequestBuilder;
import com.zs.project.service.GptService;
import com.zs.project.service.ResultCallback;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.CompletableFuture;

import static com.theokanning.openai.service.OpenAiService.*;

/**
 * @author ShuaiZhang
 * 需要用流式处理(已支持)
 */
@Service
@Slf4j
public class GptServiceImpl implements GptService {

    /**
     * openai的token
     */
    String token = "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";
    /**
     * 代理服务器
     */
    String proxyHost = "127.0.0.1";
    /**
     * 代理端口
     */
    int proxyPort = 8118;

    @Value("${openai.token}")
    private String OPENAI_KEYS;


    RestTemplate restTemplate;
    @Autowired
    public GptServiceImpl(@Qualifier("restTemplateWithProxy") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 阻塞式调用gpt3.5
     * @param query 你的promote
     * @return 结果
     */


    @Override
    public String GptResponse(String query) {
        //建造者模式创建实体
        HttpEntity<Map<String, Object>> entity = new OpenAIRequestBuilder(OPENAI_KEYS).withContent(query).buildRequestEntity();
        ResponseEntity<String> response = restTemplate.exchange(OpenAIRequestBuilder.OPENAI_ENDPOINT_URL, HttpMethod.POST, entity, String.class);
        //我个人倾向这里手动进行解析而不是封装成DTO
        //封装成DTO要处理的字段很多
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try{
            rootNode = objectMapper.readTree(response.getBody());
        }
        catch (JsonProcessingException e){
            throw new ServiceException(ErrorCode.SYSTEM_ERROR,"Json解析失败");
        }

        JsonNode choicesNode = rootNode.path("choices");
        JsonNode firstChoiceNode = choicesNode.get(0);
        JsonNode messageNode = firstChoiceNode.path("message");
        String gptResponse = messageNode.path("content").asText();
        return gptResponse;
    }

    /**
     * 流式传输
     * @param prompt 用户问题
     * @param sseEmitter 实时推送
     */


    @Override
    @Async
    public void streamChatCompletion(String prompt, SseEmitter sseEmitter) {
        streamChatCompletion(prompt, sseEmitter, result -> {});
    }

    /**
     * 关闭链接
     * @param sseEmitter
     */

    @Override
    public void sendStopEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("stop").data(""));
        } catch (Exception e){
            throw new ServiceException(ErrorCode.SYSTEM_ERROR,"事件停止接口出错");
        }
    }

    /**
     * 创建一个OpenAI服务
     * @param token API key
     * @param proxyHost 代理服务器地址
     * @param proxyPort 代理端口地址
     * @return
     */
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

    /**
     * 支持回调
     * @param prompt 用户问题
     * @param sseEmitter SSE对象
     * @param resultCallback 回调接口
     */
    @Override
    @Async
    public void streamChatCompletion(String prompt, SseEmitter sseEmitter, ResultCallback resultCallback) {
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
                    log.info("收到消息：" + choice);
                    if (StringUtils.isEmpty(choice.getFinishReason())) {
                        //未结束时才可以发送消息（结束后，先调用doOnComplete然后还会收到一条结束消息，因连接关闭导致发送消息失败:ResponseBodyEmitter has already completed）
                        sseEmitter.send(choice.getMessage());
                    }
                    String content = choice.getMessage().getContent();
                    content = content == null ? StringUtils.EMPTY : content;
                    receiveMsgBuilder.append(content);
                });
        log.info("收到的完整消息：" + receiveMsgBuilder);
        resultCallback.onCompletion(receiveMsgBuilder.toString());
    }

}
