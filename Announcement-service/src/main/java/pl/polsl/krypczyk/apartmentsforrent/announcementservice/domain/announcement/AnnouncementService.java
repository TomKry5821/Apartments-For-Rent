package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.UpdateAnnouncementResponse;

import java.util.Collection;

public interface AnnouncementService {
    Collection<AnnouncementDTO> getAllActiveAnnouncements();

    GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId);

    AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                  Long requesterId);

    UpdateAnnouncementResponse updateAnnouncement(UpdateAnnouncementRequest updateAnnouncementRequest,
                                                  Long announcementId,
                                                  Long requesterId);

}
