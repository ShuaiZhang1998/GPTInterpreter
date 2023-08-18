package com.zs.project.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * @Author ShuaiZhang
 * 代码解释器插件的开发
 */
public interface CodeInterpreter {
    /**
     * 检查用户的问题是否合法
     * @param resultsStore
     * @return json格式 {state:"",code:""}
     */
    String checkProblem(String msgId,Map<String, String> resultsStore);

    String getResult(String code);


}
