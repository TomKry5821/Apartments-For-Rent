package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;

public interface ResponseFactory {

    AnnouncementDTO createAnnouncementDTO(AnnouncementEntity announcement);

    AddNewAnnouncementResponse createAddNewAnnouncementResponse(AnnouncementEntity announcement, AddNewAnnouncementRequest addNewAnnouncementRequest);

    GetAnnouncementWithAllDetailsResponse createGetAnnouncementWithAllDetailsResponse(AnnouncementEntity announcement);

    UpdateAnnouncementResponse createUpdateAnnouncementResponse(UpdateAnnouncementRequest updateAnnouncementRequest);

    ObserveAnnouncementResponse createObserveAnnouncementResponse(Long userId, Long announcementId);

    ObservedAnnouncementDTO createObservedAnnouncementDTO(AnnouncementEntity announcement);
}
