package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.observedannouncement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils.AnnouncementUtils;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ObservedAnnouncementServiceImpl implements ObservedAnnouncementService {

    private final ObservedAnnouncementRepository observedAnnouncementRepository;

    private final EntityFactory entityFactory;

    private final ResponseFactory responseFactory;

    private final AnnouncementUtils announcementUtils;

    @Override
    public ObserveAnnouncementResponse observeAnnouncement(Long announcementId, Long userId) throws AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException {
        log.info("Started observing announcement with id - " + announcementId + " by user with id - " + userId);

        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        if (this.observedAnnouncementRepository.existsByAnnouncementEntityAndObservingUserId(announcement, userId))
            throw new AnnouncementAlreadyObservedException();
        if (announcement.getIsClosed())
            throw new ClosedAnnouncementException();


        var observedAnnouncement = this.entityFactory.createObservedAnnouncementEntity(announcement, userId);
        this.observedAnnouncementRepository.save(observedAnnouncement);

        log.info("Successfully observed announcement with id - " + announcementId + " by user with id - " + userId);
        return this.responseFactory.createObserveAnnouncementResponse(observedAnnouncement.getObservingUserId(), announcementId);
    }

    @Override
    public void unobserveAnnouncement(Long announcementId, Long userId) throws AnnouncementNotFoundException {
        log.info("Started unobserving announcement with id - " + announcementId + " by user with id - " + userId);

        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);

        this.observedAnnouncementRepository.removeObservedAnnouncementEntityByAnnouncementEntityAndObservingUserId(announcement, userId);

        log.info("Successfully unobserved announcement with id" + announcementId + " by user with id - " + userId);
    }

    @Override
    public void deleteObservedAnnouncements(Long userId) {
        log.info("Started deleting observed announcements with user id " + userId);

        this.observedAnnouncementRepository.removeObservedAnnouncementEntitiesByObservingUserId(userId);

        log.info("Successfully deleted observed announcements with user id " + userId);
    }

    @Override
    public Collection<ObservedAnnouncementDTO> getObservedAnnouncements(Long userId) {
        log.info("Started retrieving observed announcements for user with id " + userId);

        var observedAnnouncements = this.observedAnnouncementRepository.findObservedAnnouncementEntitiesByObservingUserId(userId);
        Collection<ObservedAnnouncementDTO> observedAnnouncementDTOS = new ArrayList<>();

        observedAnnouncements.forEach(oa -> observedAnnouncementDTOS.add(this.responseFactory.createObservedAnnouncementDTO(oa.getAnnouncementEntity())));

        log.info("Successfully retrieved observed announcements for user with id " + userId);
        return observedAnnouncementDTOS;
    }

}
