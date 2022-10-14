package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AnnouncementListenerTest {

    @Autowired
    AnnouncementListener announcementListener;

    @Test
    void testCloseUserAnnouncementsListenerWithValidUserId() {
        this.announcementListener.closeUserAnnouncementsListener(1L);
    }

    @Test
    void deleteUserAnnouncementsListenerWithValidUserId() {
        this.announcementListener.deleteUserAnnouncementsListener(1L);
    }

    @Test
    void deleteUserObservedAnnouncementsListenerWithValidUserId() {
        this.announcementListener.deleteObservedAnnouncementListener(1L);
    }
}