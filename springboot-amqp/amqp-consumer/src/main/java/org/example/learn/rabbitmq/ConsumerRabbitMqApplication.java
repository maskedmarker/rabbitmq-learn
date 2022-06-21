package org.example.learn.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消费者启动类
 */
@SpringBootApplication
public class ConsumerRabbitMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerRabbitMqApplication.class,args);
    }
}
