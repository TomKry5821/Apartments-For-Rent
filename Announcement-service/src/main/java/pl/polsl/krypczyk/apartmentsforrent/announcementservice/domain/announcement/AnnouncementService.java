package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.CreateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.CreateAnnouncementResponse;

import java.util.Collection;

public interface AnnouncementService {
    Collection<AnnouncementDTO> getAllAnnouncements();
    CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest createAnnouncementRequest);
}
