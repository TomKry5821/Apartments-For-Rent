package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAnnouncementConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerWithUserIdConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return properties;
    }

    // SECOND ARGUMENT IS WHAT WE WANT TO PRODUCE(OBJECT, OUR CLASS etc.)
    @Bean
    public ConsumerFactory<String, Long> consumerWithUserIdFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerWithUserIdConfig(), new StringDeserializer(), new LongDeserializer());
    }

    // SECOND ARGUMENT IS WHAT WE WANT TO PRODUCE(OBJECT, OUR CLASS etc.)
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Long>> kafkaListenerWithUserIdContainerFactory(ConsumerFactory<String, Long> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
