package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementListener {

    // IF DATA WILL BE OUR CUSTOM CLASS WE WILL PASS THERE THAT CLASS
    @KafkaListener(topics = "topic", groupId = "test", containerFactory = "kafkaListenerContainerFactory")
    void listener(String test) {
        System.out.println(test);
    }
}
