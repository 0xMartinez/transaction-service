package com.service.transaction_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.completed}")
    private String completedTopic;

    @Value("${spring.kafka.topics.expired}")
    private String expiredTopic;

    @Bean
    public NewTopic completedTransactionsTopic() {
        return new NewTopic(completedTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic expiredTransactionsTopic() {
        return new NewTopic(expiredTopic, 2, (short) 1);
    }
}