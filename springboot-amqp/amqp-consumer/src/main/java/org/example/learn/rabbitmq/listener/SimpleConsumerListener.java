package org.example.learn.rabbitmq.listener;

import org.example.learn.rabbitmq.comm.constant.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleConsumerListener {

    private static Logger log = LoggerFactory.getLogger(SimpleConsumerListener.class);

    /**
     * 监听某个队列的消息
     * @param message 接收到的消息
     */
    @RabbitListener(queues = {RabbitMqConstant.SIMPLE_QUEUE_NAME})
    public void myListener(String message){
        //不用在手动转UTF-8 Spring自动转好了
        log.debug("消费者接收到的消息为：{}", message);
        System.out.println("消费者接收到的消息为message = " + message);
    }
}
