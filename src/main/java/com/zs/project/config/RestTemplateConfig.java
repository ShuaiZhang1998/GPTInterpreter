package com.zs.project.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author GPT4
 * 自动注入restTemplate，默认的Content-TYPE为json
 * 如果在使用restTemplate时需要修改Content-TYPE，则在调用端覆盖即可
 *
 * HttpHeaders headers = new HttpHeaders();
 * headers.setContentType(MediaType.APPLICATION_JSON);
 * HttpEntity<ExecuteRequest> httpEntity = new HttpEntity<>(request, headers);
 *
 * 默认的注入不使用代理，如果希望带有代理，则使用如下方式
 * @Autowired
 * @Qualifier("restTemplateWithProxy")
 */
@Configuration
public class RestTemplateConfig {

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private int proxyPort;

    /**
     * 默认的RestTemplate，不包含代理
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add interceptor to set the Content-Type header for every request
        restTemplate.getInterceptors().add(new ContentTypeInterceptor());

        return restTemplate;
    }

    /**
     * 包含代理的RestTemplate
     * @return RestTemplate
     */
    @Bean(name = "restTemplateWithProxy")
    public RestTemplate restTemplateWithProxy() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactoryWithProxy());


        restTemplate.getInterceptors().add(new ContentTypeInterceptor());

        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactoryWithProxy() {
        RequestConfig config = RequestConfig.custom()
                .setProxy(new HttpHost(proxyHost, proxyPort))
                .build();

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }

    private static class ContentTypeInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        }
    }
}
