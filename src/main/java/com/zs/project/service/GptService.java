package com.zs.project.service;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CompletableFuture;

public interface GptService {
    String GptResponse(String query);

    void streamChatCompletion(String prompt, SseEmitter sseEmitter);

    void sendStopEvent(SseEmitter sseEmitter);

    OpenAiService buildOpenAiService(String token, String proxyHost, int proxyPort);

    void streamChatCompletion(String prompt, SseEmitter sseEmitter,ResultCallback resultCallback);



}
