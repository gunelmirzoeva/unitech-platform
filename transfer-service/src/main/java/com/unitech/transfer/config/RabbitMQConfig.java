package com.unitech.transfer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("transfer.exchange");
    }

    @Bean
    public Queue transferQueue() {
        return new Queue("transferQueue");
    }

    @Bean
    public Binding binding(Queue transferQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(transferQueue).to(topicExchange).with("transfer.success");
    }
}
