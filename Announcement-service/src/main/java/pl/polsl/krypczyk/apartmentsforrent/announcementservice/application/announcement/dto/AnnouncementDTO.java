package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcementdetails.dto.AnnouncementDetailsDTO;

import java.time.LocalDate;


@Data
@Builder
@ToString
public class AnnouncementDTO {

    private Long userId;

    private String username;

    private LocalDate creationDate;

    private AnnouncementDetailsDTO announcementDetailsDTO;

    private Boolean isClosed;

    private String district;

    private String city;
}
