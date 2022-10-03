package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private final String USER_ANNOUNCEMENT_TOPIC = "user-announcement";

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(USER_ANNOUNCEMENT_TOPIC).build();
    }
}
