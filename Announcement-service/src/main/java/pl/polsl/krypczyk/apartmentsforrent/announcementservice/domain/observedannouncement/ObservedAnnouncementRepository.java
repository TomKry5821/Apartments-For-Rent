package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservedAnnouncementRepository extends JpaRepository<ObservedAnnouncementEntity, Long> {
}
