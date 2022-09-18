package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

@Repository
public interface PhotoPathRepository extends JpaRepository<PhotoPathEntity, Long> {
}
