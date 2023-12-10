package org.example.learn.rabbitmq.listener;

import org.example.learn.rabbitmq.comm.constant.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * org.springframework.amqp.rabbit.annotation.RabbitListener#containerFactory()如果不指定的话,会使用默认的值,即
 * org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor#DEFAULT_RABBIT_LISTENER_CONTAINER_FACTORY_BEAN_NAME
 */
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
        log.info("消费者{}接收到的消息为：{}", this, message);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
