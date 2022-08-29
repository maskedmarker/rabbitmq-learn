package org.example.learn.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消费者启动类
 *
 * 多个consumer监听同一个queue时,每个consumer都是可以消费消息的,但是一条消息只能被一个consumer消费
 */
@SpringBootApplication
public class RabbitMqConsumer2Application {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqConsumer2Application.class,args);
    }
}
