package com.zs.project.chatEx;

import com.zs.project.service.CodeInterpreter;
import com.zs.project.service.GptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootTest
public class TestCheckProblem {
    @Autowired
    CodeInterpreter codeInterpreter;
    @Autowired
    GptService gptService;

    /**
     * 不影响异步返回的情况下，在消息结束后获取到完整的结果
     */
    @Test
    void TestCheckProblem(){
        SseEmitter sseEmitter = new SseEmitter();
        //gptService.streamChatCompletion("天是蓝色吗？",sseEmitter);
        StringBuilder stringBuilder = new StringBuilder();

        gptService.streamChatCompletion("天是蓝色吗？", sseEmitter, result -> {
            stringBuilder.append(result);
            // 在这里处理结果...
        });
        System.out.println(stringBuilder.toString());

        while(true){}
    }
}
