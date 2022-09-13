package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementphoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementPhotoRepository extends JpaRepository<AnnouncementPhotoEntity, Long> {
}
