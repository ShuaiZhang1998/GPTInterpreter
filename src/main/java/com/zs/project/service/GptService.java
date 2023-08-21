package com.zs.project.service;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author ShuaiZhang
 * GPT的API接口
 */

public interface GptService {
    /**
     * 阻塞式调用Gpt的接口
     * @param query 你的promote
     * @return
     */
    String GptResponse(String query);

    /**
     * 流式传输
     * @param prompt 用户问题
     * @param sseEmitter 实时推送
     */
    void streamChatCompletion(String prompt, SseEmitter sseEmitter);

    /**
     * 发送停止事件
     * @param sseEmitter
     */
    void sendStopEvent(SseEmitter sseEmitter);

    /**
     * GPT API
     * @param token API key
     * @param proxyHost 代理服务器地址
     * @param proxyPort 代理端口地址
     * @return
     */
    OpenAiService buildOpenAiService(String token, String proxyHost, int proxyPort);

    /**
     * 流式传输
     * 传输完成后获取完整结果
     * @param prompt 用户问题
     * @param sseEmitter SSE对象
     * @param resultCallback 回调接口
     */
    void streamChatCompletion(String prompt, SseEmitter sseEmitter,ResultCallback resultCallback);



}
