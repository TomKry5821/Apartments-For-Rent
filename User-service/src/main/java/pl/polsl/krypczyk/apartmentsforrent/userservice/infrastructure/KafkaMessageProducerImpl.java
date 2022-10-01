package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.KafkaMessageProducer;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageProducerImpl implements KafkaMessageProducer {

    private final KafkaTemplate<String, Long> kafkaWithUserIdTemplate;

    @Override
    public void sendInactivateAnnouncementsMessage(String topic, Long userId) {
        log.info("Sending inactivate announcements message for user with id " + userId + " on topic " + topic);
        this.kafkaWithUserIdTemplate.send(topic, userId);
        log.info("Successfully sent inactivate announcements message for user with id " + userId + " on topic " + topic);
    }

}
