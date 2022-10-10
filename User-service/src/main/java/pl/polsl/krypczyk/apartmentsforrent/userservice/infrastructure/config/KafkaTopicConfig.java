package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private final String INACTIVATE_ANNOUNCEMENT_TOPIC = "inactivate-announcement";
    private final String DELETE_ANNOUNCEMENT_TOPIC = "delete-announcement";
    private final String DELETE_OBSERVED_ANNOUNCEMENT_TOPIC = "delete-observed-announcement";

    @Bean
    public NewTopic inactivateAnnouncementTopic() {
        return TopicBuilder.name(INACTIVATE_ANNOUNCEMENT_TOPIC).build();
    }

    @Bean
    public NewTopic deleteAnnouncementTopic() {
        return TopicBuilder.name(DELETE_ANNOUNCEMENT_TOPIC).build();
    }

    @Bean
    public NewTopic deleteObservedAnnouncementTopic() {
        return TopicBuilder.name(DELETE_OBSERVED_ANNOUNCEMENT_TOPIC).build();
    }
}
