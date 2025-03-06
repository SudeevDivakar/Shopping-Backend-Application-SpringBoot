package com.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic orderPlacedTopic() {
        return new NewTopic("order-placed-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic orderDeliveredTopic() {
        return new NewTopic("order-delivered-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic orderCancelledTopic() {
        return new NewTopic("order-cancelled-topic", 3, (short) 1);
    }
}
