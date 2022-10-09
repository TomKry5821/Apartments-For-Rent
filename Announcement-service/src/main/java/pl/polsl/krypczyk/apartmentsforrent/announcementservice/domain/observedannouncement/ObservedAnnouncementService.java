package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

public interface ObservedAnnouncementService {

    ObserveAnnouncementResponse observeAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException;

    void unobserveAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException;

}
