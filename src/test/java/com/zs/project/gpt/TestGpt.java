package com.zs.project.gpt;

import com.zs.project.service.GptService;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestGpt {
    @Autowired
    GptService gptService;
    @Test
    void testGpt(){
        String s = gptService.GptResponse("说你好！");
        System.out.println(s);
    }
}
