package org.example.learn.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产者启动类
 */
@SpringBootApplication
public class ProviderRabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderRabbitApplication.class,args);
    }
}
