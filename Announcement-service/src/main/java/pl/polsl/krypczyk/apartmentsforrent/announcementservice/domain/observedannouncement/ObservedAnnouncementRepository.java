package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;

import javax.swing.text.StyledEditorKit;

@Repository
public interface ObservedAnnouncementRepository extends JpaRepository<ObservedAnnouncementEntity, Long> {
    Boolean existsByAnnouncementEntityAndAndObservingUserId(AnnouncementEntity announcement, Long userId);
}
