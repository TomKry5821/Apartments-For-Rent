package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AnnouncementRepository announcementRepository;

    @Override
    @Transactional
    public void deleteAnnouncement(Long announcementId) throws AnnouncementNotFoundException {
        var announcement = this.announcementRepository.findById(announcementId);
        if(announcement.isEmpty())
            throw new AnnouncementNotFoundException();
        announcement.get().getAnnouncementDetailsEntity().getAnnouncementContent().getPhotoPaths().clear();
        this.announcementRepository.delete(announcement.get());
    }
}
