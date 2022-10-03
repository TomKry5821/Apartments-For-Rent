package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;

@Component
@RequiredArgsConstructor
public class AnnouncementListener {

    private final AnnouncementService announcementService;
    // IF DATA WILL BE OUR CUSTOM CLASS WE WILL PASS THERE THAT CLASS
    @KafkaListener(topics = "topic", groupId = "test", containerFactory = "kafkaListenerWithUserIdContainerFactory")
    void listener(Long userId) {
        this.announcementService.closeUserAnnouncements(userId);
    }
}
