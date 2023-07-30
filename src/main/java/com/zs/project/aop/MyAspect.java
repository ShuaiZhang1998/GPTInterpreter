package com.zs.project.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    @Before("execution(* com.zs.project.service.*.*(..))")
    public void beforeAdvice() {
        System.out.println("Before method execution: Logging...");
    }
}
