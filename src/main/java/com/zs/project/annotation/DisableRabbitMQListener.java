package com.zs.project.annotation;

import com.zs.project.annotation.impl.DisableRabbitMQCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在监听器使用该注解则不会加载监听器的bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(DisableRabbitMQCondition.class)
public @interface DisableRabbitMQListener {
}
