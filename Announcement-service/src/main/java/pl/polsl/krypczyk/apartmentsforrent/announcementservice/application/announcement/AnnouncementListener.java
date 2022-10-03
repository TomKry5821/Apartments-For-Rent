package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;

@Component
@RequiredArgsConstructor
public class AnnouncementListener {

    private final String GROUP_ID = "apartments-for-rent";
    private final String USER_ANNOUNCEMENT_TOPIC = "user-announcement";
    private final AnnouncementService announcementService;
    // IF DATA WILL BE OUR CUSTOM CLASS WE WILL PASS THERE THAT CLASS
    @KafkaListener(topics = USER_ANNOUNCEMENT_TOPIC, groupId = GROUP_ID, containerFactory = "kafkaListenerWithUserIdContainerFactory")
    void closeUserAnnouncementsListener(Long userId) {
        this.announcementService.closeUserAnnouncements(userId);
    }
}
