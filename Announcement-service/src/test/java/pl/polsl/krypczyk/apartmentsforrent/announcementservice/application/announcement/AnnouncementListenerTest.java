package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AnnouncementListenerTest {

    @Autowired
    AnnouncementListener announcementListener;

    @Test
    void testCloseUserAnnouncementsListener_WithValidUserId() {
        this.announcementListener.closeUserAnnouncementsListener(1L);
    }

    @Test
    void deleteUserAnnouncementsListener_WithValidUserId() {
        this.announcementListener.deleteUserAnnouncementsListener(1L);
    }

    @Test
    void deleteUserObservedAnnouncementsListener_WithValidUserId() {
        this.announcementListener.deleteObservedAnnouncementListener(1L);
    }
}