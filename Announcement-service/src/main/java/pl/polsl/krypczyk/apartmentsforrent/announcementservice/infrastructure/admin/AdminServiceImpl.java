package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AnnouncementRepository announcementRepository;

    private final ObservedAnnouncementRepository observedAnnouncementRepository;

    @Override
    public void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException {
        log.info("Started deleting announcement with id - " + announcementId);
        var announcement = this.announcementRepository.findById(announcementId);

        if(announcement.isEmpty())
            throw new AnnouncementNotFoundException();

        CompletableFuture.runAsync(() -> this.observedAnnouncementRepository.removeObservedAnnouncementEntitiesByAnnouncementEntity_Id(announcementId));
        this.announcementRepository.delete(announcement.get());
        log.info("Successfully deleted announcement with id - " + announcementId);
    }
}
