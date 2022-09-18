package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;

import java.util.Collection;

public interface AnnouncementService {
    Collection<AnnouncementDTO> getAllActiveAnnouncements();

    GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) throws AnnouncementNotFoundException;

    AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                  Long requesterId) throws InvalidUserIdException;

    UpdateAnnouncementResponse updateAnnouncement(UpdateAnnouncementRequest updateAnnouncementRequest,
                                                  Long announcementId,
                                                  Long requesterId) throws AnnouncementNotFoundException, ClosedAnnouncementException;

    void closeAnnouncement(Long announcementId, Long requesterId) throws AnnouncementNotFoundException;

}
