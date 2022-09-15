package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementContentRepository extends JpaRepository<AnnouncementContentEntity, Long> {
}
