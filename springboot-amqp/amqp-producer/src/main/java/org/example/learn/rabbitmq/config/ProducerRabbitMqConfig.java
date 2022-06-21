package org.example.learn.rabbitmq.config;

import org.example.learn.rabbitmq.comm.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq 配置类
 * producer需要声明队列/交换机/绑定关系,而consumer只需要向broker发送消费特定队列的消息请求
 */
@Configuration
public class ProducerRabbitMqConfig {
    /**
     * 创建 交换机
     * @return
     */
    @Bean
    public Exchange simpleExchange(){
        return ExchangeBuilder.topicExchange(RabbitMqConstant.SIMPLE_EXCHANGE_NAME).build();
    }

    /**
     * 创建 队列
     * @return
     */
    @Bean
    public Queue simpleQueue(){
        return QueueBuilder.durable(RabbitMqConstant.SIMPLE_QUEUE_NAME).build();
    }

    /**
     * 绑定 交换机与队列
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding itemQueueExchange(@Qualifier("simpleExchange") Exchange exchange, @Qualifier("simpleQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

}