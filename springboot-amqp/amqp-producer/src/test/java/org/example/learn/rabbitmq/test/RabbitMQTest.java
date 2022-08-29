package org.example.learn.rabbitmq.test;

import org.example.learn.rabbitmq.producer.SimpleProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {

    @Resource
    private SimpleProducer simpleProducer;

    @Test
    public void sendTest() {
        for (int i = 0; i < 100; i++) {
            String message = String.format("测试 SpringBoot整合RabbitMq的普通模式,index=%d", i);
            simpleProducer.send(message);
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("springoobt exits main");
    }
}
