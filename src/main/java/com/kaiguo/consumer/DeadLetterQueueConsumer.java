package com.kaiguo.consumer;

import com.kaiguo.bean.Order;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DeadLetterQueueConsumer {

    public static final String DEAD_QUEUE_A = "dead.queue.a";
    @RabbitListener(queues = DEAD_QUEUE_A)
    public void receiveA(Order order, Channel channel, Message message) throws IOException {
        try {
            log.info("订单号: {} 未支付, 正在取消订单", order.getOrderSn());
            /**
             * 逻辑代码
             */

            log.info("订单号: {} 取消成功", order.getOrderSn());
            // ACK 确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("订单号: {} 未支付, 取消失败", order.getOrderSn());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
