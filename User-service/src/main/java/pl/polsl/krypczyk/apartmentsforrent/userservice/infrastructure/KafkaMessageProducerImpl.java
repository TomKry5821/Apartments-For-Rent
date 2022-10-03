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

    private final String USER_ANNOUNCEMENT_TOPIC = "user-announcement";
    private final KafkaTemplate<String, Long> kafkaWithUserIdTemplate;

    @Override
    public void sendInactivateAnnouncementsMessage(Long userId) {
        log.info("Sending inactivate announcements message for user with id " + userId + " on topic " + USER_ANNOUNCEMENT_TOPIC);
        this.kafkaWithUserIdTemplate.send(USER_ANNOUNCEMENT_TOPIC, userId);
        log.info("Successfully sent inactivate announcements message for user with id " + userId + " on topic " + USER_ANNOUNCEMENT_TOPIC);
    }

}