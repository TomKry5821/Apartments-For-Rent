package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

import java.util.Collection;

public interface ObservedAnnouncementService {

    ObserveAnnouncementResponse observeAnnouncement(Long announcementId, Long userId) throws AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException;

    void unobserveAnnouncement(Long announcementId, Long userId) throws AnnouncementNotFoundException;

    void deleteObservedAnnouncements(Long userId);

    Collection<ObservedAnnouncementDTO> getObservedAnnouncements(Long userId);

}
