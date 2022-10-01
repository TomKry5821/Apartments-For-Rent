package pl.polsl.krypczyk.apartmentsforrent.userservice.domain;

public interface KafkaMessageProducer {

    void sendInactivateAnnouncementsMessage(String topic, Long userId);
}
