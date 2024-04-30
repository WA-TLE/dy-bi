package com.dy.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.dy.mq.BiMqConstant.BI_EXCHANGE_NAME;
import static com.dy.mq.BiMqConstant.BI_ROUTING_KEY;

/**
 * @Author: dy
 * @Date: 2024/4/29 11:54
 * @Description: 消息发送者
 */
@Component
public class MyMessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BI_EXCHANGE_NAME, BI_ROUTING_KEY, message);
    }


}
