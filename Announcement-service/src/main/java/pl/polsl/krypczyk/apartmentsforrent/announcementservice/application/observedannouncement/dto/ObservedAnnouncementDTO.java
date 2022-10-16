package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObservedAnnouncementDTO {

    private String title;

    private String mainPhotoPath;

    private Long userId;

}
