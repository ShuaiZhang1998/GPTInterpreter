package com.zs.project.annotation.impl;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DisableRabbitMQCondition implements Condition {
    /**
     * @Author GPT4.0
     * @param context
     * @param metadata
     * @return
     */

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 在这里实现你的条件判断逻辑
        // 返回true表示加载，返回false表示不加载
        // 你可以根据配置文件、环境变量等来决定是否加载Rabbit MQ的配置和监听器
        // 这里简单返回false来示范不加载
        return false;
    }
}
