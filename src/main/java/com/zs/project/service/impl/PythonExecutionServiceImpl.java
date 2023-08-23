package com.zs.project.service.impl;

import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.model.dto.execution.ExecuteRequest;
import com.zs.project.service.ExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ShuaiZhang
 * python代码解释器
 */


@Service
public class PythonExecutionServiceImpl implements ExService {


    /**
     * 执行python服务的地址
     */
    @Value("${python.executor.url}")
    private String executeApiUrl;

    /**
     * 发post请求
     */
    private final RestTemplate restTemplate;

    /**
     * 4.3版本目标ben只有一个构造器Autowired可以省略
     * @param restTemplate
     */
    @Autowired
    public PythonExecutionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 最基础的版本，支持python3.9运行
     * @param code 需要被执行的代码
     * @return 运行结果
     */
    @Override
    public String resultExPython(String code) {
        ExecuteRequest request = new ExecuteRequest("python",code);
        HttpEntity<ExecuteRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                executeApiUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ServiceException(ErrorCode.SYSTEM_ERROR,"执行器错误");
        }
        return response.getBody();
    }
}
