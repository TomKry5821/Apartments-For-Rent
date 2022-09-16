package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto;

import lombok.Data;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.dto.AnnouncementDetailsDTO;

import java.time.LocalDate;


@Data
public class AnnouncementDTO {

    private Long userId;

    private LocalDate creationDate;

    private AnnouncementDetailsDTO announcementDetailsDTO;

    private Boolean isClosed;
}
