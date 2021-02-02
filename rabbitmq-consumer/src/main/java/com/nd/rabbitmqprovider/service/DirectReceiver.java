package com.nd.rabbitmqprovider.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Service;


@Component
public class DirectReceiver {

//	@RabbitListener(queues = "order.topic")
//    public void process(Channel channel, Message message,String map) {
//		try {
//			//channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//
//			System.out.println("DirectReceiver消费者收到消息  : " + message.toString());
//			int i = 1/0;
//			System.out.println("DirectReceiver消费者收到消息  : " + map.toString());
//		} catch (Exception e) {
//			try {
//				channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);  //异常，把当前消息重新入队列
//				//channel.basicRecover(true);  //异常，把当前消息重新入队列，true：将尽可能的把消息投递给其他消费者;  false：消息会被重新投递给自己
//				//channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
//			} catch (IOException ioException) {
//				ioException.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//    }

    @RabbitListener(queues = "dead.topic")
    public void deadLetterQueue(Channel channel, Message message,String map){
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("死信队列 消费者收到消息  : " + message.toString());
		System.out.println("死信队列 消费者收到消息  : " + map.toString());

	}

}
