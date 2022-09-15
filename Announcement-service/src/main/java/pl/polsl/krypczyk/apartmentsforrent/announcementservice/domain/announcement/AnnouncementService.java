package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;

import java.util.Collection;

public interface AnnouncementService {
    Collection<AnnouncementDTO> getAllAnnouncements();
}
