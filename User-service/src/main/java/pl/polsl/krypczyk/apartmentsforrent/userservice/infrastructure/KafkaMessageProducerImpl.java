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

    private final String INACTIVATE_ANNOUNCEMENT_TOPIC = "inactivate-announcement";
    private final String DELETE_ANNOUNCEMENT_TOPIC = "delete-announcement";
    private final String DELETE_OBSERVED_ANNOUNCEMENT_TOPIC = "delete-observed-announcement";
    private final KafkaTemplate<String, Long> kafkaWithUserIdTemplate;

    @Override
    public void sendInactivateAnnouncementsMessage(Long userId) {
        log.info("Sending inactivate announcements message for user with id " + userId + " on topic " + INACTIVATE_ANNOUNCEMENT_TOPIC);
        this.kafkaWithUserIdTemplate.send(INACTIVATE_ANNOUNCEMENT_TOPIC, userId);
    }

    @Override
    public  void sendDeleteAnnouncementMessage(Long userId){
        log.info("Sending delete announcements message for user with id " + userId + " on topic " + DELETE_ANNOUNCEMENT_TOPIC);
        this.kafkaWithUserIdTemplate.send(DELETE_ANNOUNCEMENT_TOPIC, userId);
    }

    @Override
    public  void sendDeleteObservedAnnouncementMessage(Long userId){
        log.info("Sending delete observed announcements message for user with id " + userId + " on topic " + DELETE_OBSERVED_ANNOUNCEMENT_TOPIC);
        this.kafkaWithUserIdTemplate.send(DELETE_OBSERVED_ANNOUNCEMENT_TOPIC, userId);
    }

}
