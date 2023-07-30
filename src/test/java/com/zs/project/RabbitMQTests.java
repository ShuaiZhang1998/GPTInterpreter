package com.zs.project;

import com.zs.project.config.RabbitMQConfig;
import com.zs.project.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

@SpringBootTest

class RabbitMQTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 最基础的发送
     */
    @Test
    void testSend() {
        Long start = System.currentTimeMillis();
        for(int i=0;i<1000;i++)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.error","Test");
        Long end = System.currentTimeMillis();
        System.out.println((end-start)/1000);
    }

    /**
     * 基础的消息接受
     * 这个方法取消息很慢
     */
    @Test
    void testGet(){
        for(int i=0;i<1000;i++){
            rabbitTemplate.receiveAndConvert(RabbitMQConfig.QUEUE_NAME);
        }
    }

    /**
     * 消息可靠投递之 确认模式
     * @throws InterruptedException
     */
    @Test
    void testSendConfirm() throws InterruptedException {
        //需要在配置文件中开启确认模式
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关配置信息（在发送消息的时候设置）
             * @param b 交换机是否接收到信息
             * @param s 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("confirm方法被执行了，b is "+b+" s is "+s);
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.error","Test");
        System.out.println("in");
    }

    /**
     * 消息可靠投递之 回退模式
     */
    @Test
    void testReturn(){
        //首先确保配置文件开启了返回模式
        //交换机默认会把路由失败的消息丢弃，希望触发回调需要改变模式
        rabbitTemplate.setMandatory(true);
        //不推荐使用setReturnCallback
        rabbitTemplate.setReturnsCallback((message)->{
            System.out.println("路由失败，回调");
            System.out.println(message);
        });
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,"boot.error","Test");
    }
    @Autowired
    TestService testService;

    @Test
    void testAOP(){
        testService.testServiece();
    }

}
