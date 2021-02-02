package com.nd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MqAckSender implements ApplicationRunner {

    private Logger logger= LoggerFactory.getLogger(RabbitTemplate.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 生产者发送消息confirm检测
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                logger.info("ackMQSender 消息发送失败" + cause + correlationData.toString());
            } else {
                logger.info("ackMQSender 消息发送成功 ");
            }
        });

        // 生产者发送消息到exchange后没有绑定的queue时将消息退回
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            logger.info("ackMQSender 发送消息被退回" + exchange + routingKey);
        });
    }
}
