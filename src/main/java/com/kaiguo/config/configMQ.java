package com.kaiguo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class configMQ {
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_QUEUE_ROUTING_KEY_A = "delay.queue.a.routingkey";
    public static final String DELAY_QUEUE_ROUTING_KEY_B = "delay.queue.b.routingkey";
    public static final String DELAY_QUEUE_A = "delay.queue.a";
    public static final String DELAY_QUEUE_B = "delay.queue.b";

    public static final String DEAD_EXCHANGE = "dead.exchange";
    public static final String DEAD_QUEUE_ROUTING_KEY_A = "dead.queue.a.routingkey";
    public static final String DEAD_QUEUE_ROUTING_KEY_B = "dead.queue.b.routingkey";
    public static final String DEAD_QUEUE_A = "dead.queue.a";
    public static final String DEAD_QUEUE_B = "dead.queue.b";

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean("orderDelayExchange")
    public DirectExchange orderDelayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    @Bean("orderDeadExchange")
    public DirectExchange orderDeadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean("orderDelayQueueA")
    public Queue orderDelayQueueA(){
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", DEAD_QUEUE_ROUTING_KEY_A);
        //声明队列的 TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(DELAY_QUEUE_A).withArguments(args).build();
    }

    @Bean("orderDelayBindingA")
    public Binding orderDelayBindingA(@Qualifier("orderDelayExchange") DirectExchange orderDelayExchange,
                                 @Qualifier("orderDelayQueueA") Queue orderDelayQueueA){
        return BindingBuilder.bind(orderDelayQueueA).to(orderDelayExchange).with(DELAY_QUEUE_ROUTING_KEY_A);
    }

    @Bean("orderDelayQueueB")
    public Queue orderDelayQueueB(){
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", DEAD_QUEUE_ROUTING_KEY_B);
        //声明队列的 TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(DELAY_QUEUE_B).withArguments(args).build();
    }

    @Bean("orderDelayBindingB")
    public Binding delayBindingB(@Qualifier("orderDelayExchange") DirectExchange directExchange,
                                 @Qualifier("orderDelayQueueB") Queue orderDelayQueueB){
        return BindingBuilder.bind(orderDelayQueueB).to(directExchange).with(DELAY_QUEUE_ROUTING_KEY_B);
    }

    @Bean("orderDeadLetterQueueA")
    public Queue deadLetterQueueA(){
        return QueueBuilder.durable(DEAD_QUEUE_A).build();
    }

    @Bean("orderDeadLetterBindingA")
    public Binding orderDeadLetterBindingA(@Qualifier("orderDeadExchange") DirectExchange orderDeadExchange,
                                           @Qualifier("orderDeadLetterQueueA") Queue orderDeadLetterQueueA){
        return BindingBuilder.bind(orderDeadLetterQueueA).to(orderDeadExchange).with(DEAD_QUEUE_ROUTING_KEY_A);
    }

    @Bean("orderDeadLetterQueueB")
    public Queue deadLetterQueueB(){
        return QueueBuilder.durable(DEAD_QUEUE_B).build();
    }

    @Bean("orderDeadLetterBindingA")
    public Binding orderDeadLetterBindingB(@Qualifier("orderDeadExchange") DirectExchange orderDeadExchange,
                                           @Qualifier("orderDeadLetterQueueB") Queue orderDeadLetterQueueB){
        return BindingBuilder.bind(orderDeadLetterQueueB).to(orderDeadExchange).with(DEAD_QUEUE_ROUTING_KEY_B);
    }
}
