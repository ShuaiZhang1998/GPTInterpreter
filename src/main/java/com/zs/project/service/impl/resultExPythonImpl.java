package com.zs.project.service.impl;

import com.zs.project.exception.ErrorCode;
import com.zs.project.exception.ServiceException;
import com.zs.project.service.ExService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

/**
 * @author ShuaiZhang
 * pthon代码解释器
 */


@Service
public class resultExPythonImpl implements ExService {
    /**
     * 最基础的版本，只有一个python3.9
     * @param code
     * @return
     */
    @Override
    public String resultExPython(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://10.211.55.2:8000/execute";
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("language", "python");
        requestBodyJson.put("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
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
