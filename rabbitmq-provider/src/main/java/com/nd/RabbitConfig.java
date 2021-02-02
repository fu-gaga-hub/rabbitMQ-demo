package com.nd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	private final String order = "order.topic";
	
	private final String user = "user.topic";

	private final String deadLetter = "dead.topic";

    //队列 起名：TestDirectQueue
    @Bean
    public Queue userQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);
 
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(user,true);
    }

    //业务队列绑定死信交换机和死信队列，消息basicReject或者basicNack之后会自动将消息发送到私信队列
    @Bean
    public Queue orderQueue() {
        Map<String,Object> args = new HashMap<>();
        //       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", "deadLetterExchange");
        //       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", "dead.topic");

        return QueueBuilder.durable(order).withArguments(args).build();
    }

    //声明死信队列
    @Bean
    public Queue deadLetterQueue(){
        return new Queue(deadLetter,true);
    }
 
//    //Direct交换机 起名：TestDirectExchange
//    @Bean
//    public DirectExchange TestDirectExchange() {
//      //  return new DirectExchange("TestDirectExchange",true,true);
//        return new DirectExchange("TestDirectExchange");
//    }
    
    //Topic交换机 起名：TestTopicExchange
    @Bean
    public TopicExchange TestTopicExchange() {
      //  return new DirectExchange("TestDirectExchange",true,true);
        return new TopicExchange("TestTopicExchange");
    }
    
//    //延时队列插件交换机 起名：delayedExchange
//    @Bean
//    public FanoutExchange delayedExchange() {
//    	 Map<String, Object> args = new HashMap<String, Object>();
//         args.put("x-delayed-type", "direct");
//         FanoutExchange topicExchange = new FanoutExchange("delayedExchange", true, false, args);
//         topicExchange.setDelayed(true);
//         return topicExchange;
//    }

    //声明死信交换机
    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange("deadLetterExchange");
    }

    
 
    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    public Binding bindingDirectUser() {
        return BindingBuilder.bind(orderQueue()).to(TestTopicExchange()).with("*.topic");
    }
    
    @Bean
    public Binding bindingDirectOrder() {
        return BindingBuilder.bind(orderQueue()).to(TestTopicExchange()).with("order.*");
    }

    //死信绑定私信交换机
    @Bean
    public Binding bindingDeadLetter(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dead.topic");
    }
    
 
//	  /**
//	   * 绑定延时交换机
//	   * @author fuGaga
//	   * @date 2020年10月30日
//	   * @return
//	   */
//      @Bean
//      public Binding bindingDelayed(){
//    	  return BindingBuilder.bind(orderQueue()).to(delayedExchange());
//      }
 
 
//    @Bean
//    public DirectExchange lonelyDirectExchange() {
//        return new DirectExchange("lonelyDirectExchange");
//    }

}
