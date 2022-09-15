package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.dto.AnnouncementDetailsDTO;

@Mapper
public interface AnnouncementMapper {
    AnnouncementDetailsDTO announcementDetailsEntityToAnnouncementDetailsDTO(AnnouncementDetailsEntity announcementDetailsEntity);
    AnnouncementDTO announcementEntityToAnnouncementDTO(AnnouncementEntity announcementEntity);
}
