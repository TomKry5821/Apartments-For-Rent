package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto;

import lombok.Data;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.dto.AnnouncementDetailsDTO;


@Data
public class AnnouncementDTO {

    private Long userId;

    private AnnouncementDetailsDTO announcementDetailsDTO;
}
