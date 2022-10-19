package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;

import java.util.Collection;

public interface AnnouncementService {
    Collection<AnnouncementDTO> getAllActiveAnnouncements();

    GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) throws AnnouncementNotFoundException;

    AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest);

    UpdateAnnouncementResponse updateAnnouncement(UpdateAnnouncementRequest updateAnnouncementRequest,
                                                  Long announcementId) throws AnnouncementNotFoundException, ClosedAnnouncementException;

    void closeAnnouncement(Long announcementId, Long userId) throws AnnouncementNotFoundException;

    void closeUserAnnouncements(Long userId);

    void deleteUserAnnouncements(Long userId);

}
