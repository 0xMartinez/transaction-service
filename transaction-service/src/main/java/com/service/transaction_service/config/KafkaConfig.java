package com.service.transaction_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic completedTransactionsTopic() {
        return new NewTopic("transakcje-zrealizowane", 3, (short) 2);
    }

    @Bean
    public NewTopic expiredTransactionsTopic() {
        return new NewTopic("transakcje-przeterminowane", 3, (short) 2);
    }
}