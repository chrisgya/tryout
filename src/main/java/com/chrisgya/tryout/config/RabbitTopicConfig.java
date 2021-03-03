package com.chrisgya.tryout.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopicConfig {
    public static final String TOPIC_EXCHANGE_NAME = "demoAppTopicExchange";
    public static final String SEND_USER_VERIFICATION_EMAIL_QUEUE = "userEmailVerificationNotificationQueue";
    public static final String SEND_USER_DEACTIVATION_EMAIL_QUEUE = "userEmailDeactivationNotificationQueue";

    public static final String SEND_USER_VERIFICATION_EMAIL_ROUTING_KEY = "demo-queue.user-verification-email";
    public static final String SEND_USER_DEACTIVATION_EMAIL_ROUTING_KEY = "demo-queue.user-deactivation-email";

    @Bean
    Queue sendUserVerificationEmailQueue() {
        return new Queue(SEND_USER_VERIFICATION_EMAIL_QUEUE, true);
    }

    @Bean
    Queue sendUserDeactivationEmailQueue() {
        return new Queue(SEND_USER_DEACTIVATION_EMAIL_QUEUE, true);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding sendUserVerificationEmailBinding(Queue sendUserVerificationEmailQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(sendUserVerificationEmailQueue).to(topicExchange).with(SEND_USER_VERIFICATION_EMAIL_ROUTING_KEY);
    }

    @Bean
    Binding sendUserDeactivationEmailBinding(Queue sendUserDeactivationEmailQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(sendUserDeactivationEmailQueue).to(topicExchange).with(SEND_USER_DEACTIVATION_EMAIL_ROUTING_KEY);
    }

}
