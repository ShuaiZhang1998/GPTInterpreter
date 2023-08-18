package com.zs.project.config;

import com.zs.project.annotation.DisableRabbitMQListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Ahtuor ShuaiZhang
 */
@Configuration

public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "bootTopicExchange";
    public static final String QUEUE_NAME = "bootQueue";

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    /**
     *  创建一个交换机
     *  ExchangeBuilder可以建立四种不同的交换机（直接，主题，订阅发布，头模式）
     * @return
     */
    @Bean("bootExchange")
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("bootQueue")
    public Queue queue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue,
                                     @Qualifier("bootExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

    /**
     * 测了一下不加这个Bean，发送100000条数据耗时是一致的
     * 开启确认模式后250/s一条
     * @return
     */
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        //开启确认Confirm模式
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        //开启回退Return模式
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }
    /**
     * 控制每次只接受一条
     */

    @Bean
    @DisableRabbitMQListener

    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        // 设置每个消费者每次只取一条消息
        factory.setPrefetchCount(1);
        return factory;
    }


}
