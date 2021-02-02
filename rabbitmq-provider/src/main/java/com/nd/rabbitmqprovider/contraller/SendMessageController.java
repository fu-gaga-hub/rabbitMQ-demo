package com.nd.rabbitmqprovider.contraller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.nd.util.MqAckSender;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @Autowired
    MqAckSender mqAckSender;
 
    @GetMapping("/sendDirectMessageUser")
    public String sendDirectMessageUser() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
//        rabbitTemplate.convertAndSend("TestTopicExchange", "order.#", map);
        rabbitTemplate.convertAndSend("TestTopicExchange","order.abc",map.toString());
        return "ok";
    }

    @GetMapping("/sendDirectMessageOrder")
    public String sendDirectMessageOrder() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestTopicExchange", "order.abc", map.toString());
        //rabbitTemplate.convertSendAndReceive("deadLetterExchange","dead.topic", map.toString());
        return "ok";
    }
    
    @GetMapping("/sendDirectMessageDelayed")
    public String sendDirectMessageDelayed() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("delayedExchange", "", map, new MessagePostProcessor(){
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setHeader("x-delay", 5000);
				return message;
			}
        });
        return "ok";
    }
}
