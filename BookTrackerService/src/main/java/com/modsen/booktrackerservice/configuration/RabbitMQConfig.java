package com.modsen.booktrackerservice.configuration;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "Library-queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true, false,true);
    }


}
