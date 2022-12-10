package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ObservedAnnouncementDTO {

    private Long announcementId;

    private String title;

    private byte[] mainPhoto;

    private Long userId;

    private String username;

}
