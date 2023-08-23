package com.zs.project.model.dto.execution;

import lombok.Data;

/**
 * @author ShuaiZhang
 * 代码解释服务的请求体
 */
@Data
public class ExecuteRequest {
    /**
     * 解释器所使用的语言
     */
    private String language;

    /**
     * 具体要执行的代码
     */
    private String code;

    public ExecuteRequest(String language, String code) {
        this.language = language;
        this.code = code;
    }
}
