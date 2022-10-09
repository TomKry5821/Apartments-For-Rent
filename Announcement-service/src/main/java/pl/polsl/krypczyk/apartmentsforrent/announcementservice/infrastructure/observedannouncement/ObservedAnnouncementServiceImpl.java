package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.observedannouncement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils.AnnouncementUtils;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils.UserUtils;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ObservedAnnouncementServiceImpl implements ObservedAnnouncementService {

    private final ObservedAnnouncementRepository observedAnnouncementRepository;

    private final EntityFactory entityFactory;

    private final ResponseFactory responseFactory;

    private final UserUtils userUtils;

    private final AnnouncementUtils announcementUtils;

    @Override
    public ObserveAnnouncementResponse observeAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException {
        log.info("Started observing announcement with id - " + announcementId + " by user with id - " + userId);
        this.userUtils.checkIsUserIdValidElseThrowInvalidUser(userId, requesterId);

        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        if (this.observedAnnouncementRepository.existsByAnnouncementEntityAndObservingUserId(announcement, userId))
            throw new AnnouncementAlreadyObservedException();
        if (announcement.getIsClosed())
            throw new ClosedAnnouncementException();


        var observedAnnouncement = this.entityFactory.createObservedAnnouncementEntity(announcement, userId);

        log.info("Successfully observed announcement with id - " + announcementId + " by user with id - " + userId);
        return this.responseFactory.createObserveAnnouncementResponse(observedAnnouncement.getObservingUserId(), announcementId);
    }

    @Override
    public void unobserveAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException {
        log.info("Started unobserving announcement with id - " + announcementId + " by user with id - " + userId);

        this.userUtils.checkIsUserIdValidElseThrowInvalidUser(userId, requesterId);
        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);

        this.observedAnnouncementRepository.removeObservedAnnouncementEntityByAnnouncementEntityAndObservingUserId(announcement, userId);

        log.info("Successfully unobserved announcement with id" + announcementId + " by user with id - " + userId);
    }

}
