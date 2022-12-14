package pl.polsl.krypczyk.apartmentsforrent.userservice.domain;

public interface KafkaMessageProducer {

    void sendInactivateAnnouncementsMessage(Long userId);

    void sendDeleteAnnouncementMessage(Long userId);

    void sendDeleteObservedAnnouncementMessage(Long userId);
}
