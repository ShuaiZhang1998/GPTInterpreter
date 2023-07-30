package com.zs.project.annotation;

import com.zs.project.annotation.impl.DisableRabbitMQCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(DisableRabbitMQCondition.class)
public @interface DisableRabbitMQListener {
}
