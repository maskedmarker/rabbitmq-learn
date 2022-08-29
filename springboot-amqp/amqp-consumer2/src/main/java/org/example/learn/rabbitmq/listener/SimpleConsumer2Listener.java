package org.example.learn.rabbitmq.listener;

import org.example.learn.rabbitmq.comm.constant.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SimpleConsumer2Listener {

    private static Logger log = LoggerFactory.getLogger(SimpleConsumer2Listener.class);

    /**
     * 监听某个队列的消息
     * @param message 接收到的消息
     */
    @RabbitListener(queues = {RabbitMqConstant.SIMPLE_QUEUE_NAME})
    public void myListener(String message){
        //不用在手动转UTF-8 Spring自动转好了
        log.info("消费者{}接收到的消息为：{}", this, message);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
