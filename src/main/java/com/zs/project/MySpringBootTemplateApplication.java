package com.zs.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MySpringBootTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootTemplateApplication.class, args);
    }

}
