package com.kaiguo.controller;

import com.kaiguo.bean.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@Slf4j
public class OrderController {
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_QUEUE_ROUTING_KEY_A = "delay.queue.a.routingkey";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/order")
    public String getOrder(){
        String replace = UUID.randomUUID().toString().replace("-", "");
        Order order = new Order();
        order.setOrderSn(replace);
        rabbitTemplate.convertAndSend(DELAY_EXCHANGE, DELAY_QUEUE_ROUTING_KEY_A, order);
        log.info("下单成功订单号为: {}", replace);
        return "下单成功订单号为: " + replace;
    }
}
