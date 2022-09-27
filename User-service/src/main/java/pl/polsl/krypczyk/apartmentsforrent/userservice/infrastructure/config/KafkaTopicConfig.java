package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic").build();
    }

    /*@Bean
    public NewTopic streamTopic(){
        return TopicBuilder.name("streamTopic").build();
    }*/
}
