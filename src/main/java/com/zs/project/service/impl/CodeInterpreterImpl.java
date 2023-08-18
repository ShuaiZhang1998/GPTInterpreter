package com.zs.project.service.impl;

import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.service.CodeInterpreter;
import com.zs.project.service.ExService;
import com.zs.project.service.GptService;
import com.zs.project.util.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: ShuaiZhang
 * 代码解释器插件相关的业务
 */
@Service
public class CodeInterpreterImpl implements CodeInterpreter {
    /**
     * 用于存储回调函数返回的结果
     */
    private Map<String, String> resultsStore = new ConcurrentHashMap<>();

    @Resource
    ExService exService;
    @Resource
    GptService gptService;

    /**
     * 检查用户问题是否合法
     * 检查过程中向前端推送流
     * 必须是异步
     * @param resultsStore
     * @return
     */

    @Override
    public String checkProblem(String msgId,Map<String, String> resultsStore) {

        return "";
    }

    /**
     * @param code 包含python代码的markdown
     * @return 执行结果
     */

    @Override
    public String getResult(String code) {
        String codePython = PatternUtils.extractPythonCode(code);
        String exResult="";
        if(StringUtils.isNotBlank(codePython)){
            exResult = exService.resultExPython(codePython);
        }
        return exResult;
    }
}
