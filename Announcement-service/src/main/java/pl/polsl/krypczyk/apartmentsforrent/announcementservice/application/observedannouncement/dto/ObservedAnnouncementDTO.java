package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ObservedAnnouncementDTO {

    private String title;

    private String mainPhotoPath;

    private Long userId;

    private String username;

}
