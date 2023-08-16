package com.zs.project.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
    /**
     * 从markdown中解析出python代码
     * @param markdown
     * @return
     */
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
