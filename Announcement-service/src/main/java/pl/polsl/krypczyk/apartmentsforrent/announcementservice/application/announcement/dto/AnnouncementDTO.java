package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto;

import lombok.Data;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcementdetails.dto.AnnouncementDetailsDTO;

import java.time.LocalDate;


@Data
public class AnnouncementDTO {

    private Long userId;

    private LocalDate creationDate;

    private AnnouncementDetailsDTO announcementDetailsDTO;

    private Boolean isClosed;

    private String district;

    private String city;
}
