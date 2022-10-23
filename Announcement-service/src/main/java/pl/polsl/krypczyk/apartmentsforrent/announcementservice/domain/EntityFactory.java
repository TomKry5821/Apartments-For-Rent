package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementEntity;

public interface EntityFactory {
    AnnouncementEntity createAnnouncementEntity(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                AnnouncementDetailsEntity announcementDetails);

    AnnouncementDetailsEntity createAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                              AnnouncementContentEntity announcementContent,
                                                              AddressDetailsEntity addressDetails);

    AnnouncementContentEntity createAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);

    AddressDetailsEntity createAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest);

    ObservedAnnouncementEntity createObservedAnnouncementEntity(AnnouncementEntity announcement, Long userId);


}
