package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;

@Component
@RequiredArgsConstructor
public class AnnouncementUtils {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementEntity getAnnouncementElseThrowAnnouncementNotFound(Long announcementId) throws AnnouncementNotFoundException {
        var announcement = this.announcementRepository.findById(announcementId);
        if (announcement.isEmpty())
            throw new AnnouncementNotFoundException();
        return announcement.get();
    }
}
