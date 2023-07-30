package com.zs.project.config;

import com.rabbitmq.client.Channel;
import com.zs.project.annotation.DisableRabbitMQListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听器
 */
@DisableRabbitMQListener
@Component
public class RabbitMQStartListener {
    /**
     * 默认监听器，拿到所有数据，自动确认
     * @param message
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void Listen(Message message){
        long start = System.currentTimeMillis();
        long end =  System.currentTimeMillis();
        System.out.println((end-start)/1000);
    }

    /**
     * 采用手动应答，自动开启
     * @param message
     * @param channel
     * @throws IOException
     */
    //@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, ackMode = "MANUAL",autoStartup = "true")
    public void listen(Message message, Channel channel) throws IOException {
        System.out.println(message.getBody());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }

    /**
     * 模拟了一下每次拿一条的场景
     * @param message
     * @param channel
     * @throws IOException
     */
    //@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, ackMode = "MANUAL",autoStartup = "true")
    public void listen_(Message message,Channel channel) throws IOException {
        try {
            System.out.println(message.getBody());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            Thread.sleep(200);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
