package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementDetailsRepository extends JpaRepository<AnnouncementDetailsEntity, Long> {
}
