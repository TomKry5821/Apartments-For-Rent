package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
    Collection<AnnouncementEntity> findAnnouncementEntitiesByIsClosed(Boolean isClosed);
    Collection<AnnouncementEntity> findAnnouncementEntitiesByIsClosedAndUserId(Boolean isClosed, Long userId);

    Collection<AnnouncementEntity> findAnnouncementEntitiesByUserId(Long userId);

    void deleteAnnouncementEntitiesByUserId(Long userId);
}
