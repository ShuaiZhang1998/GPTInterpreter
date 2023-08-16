package com.zs.project.chatEx;

import com.zs.project.service.ExService;
import com.zs.project.service.GptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class TestGPTAndEx {
    @Autowired
    GptService gptService;
    @Autowired
    ExService exService;
    @Test
    void testExPython(){
        String code = gptService.GptResponse("编写一个最简单的python语言入门代码，实现对5，3，2，1，4的快速排序，不要添加代码以外的任何东西");
        System.out.println(code);
        String s = exService.resultExPython(TestGPTAndEx.extractPythonCode(code));
        System.out.println(s);
    }

    public static String extractPythonCode(String markdown) {
        List<String> codeBlocks = new ArrayList<>();
        // 正则表达式匹配 ```python``` 之间的内容，
        Pattern pattern = Pattern.compile("```python([^`]+)```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(markdown);
        if (matcher.find()) {
            return matcher.group(1).trim();  // 提取代码块内容并返回
        }
        return "";
    }
}
