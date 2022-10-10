package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;

import java.util.Collection;

@Repository
public interface ObservedAnnouncementRepository extends JpaRepository<ObservedAnnouncementEntity, Long> {
    Boolean existsByAnnouncementEntityAndObservingUserId(AnnouncementEntity announcement, Long userId);

    void removeObservedAnnouncementEntityByAnnouncementEntityAndObservingUserId(AnnouncementEntity announcement, Long userId);

    void removeObservedAnnouncementEntitiesByObservingUserId(Long observingUserId);

    void removeObservedAnnouncementEntitiesByAnnouncementEntity_Id(Long announcementId);

    Collection<ObservedAnnouncementEntity> findObservedAnnouncementEntitiesByObservingUserId(Long observingUserId);
}
