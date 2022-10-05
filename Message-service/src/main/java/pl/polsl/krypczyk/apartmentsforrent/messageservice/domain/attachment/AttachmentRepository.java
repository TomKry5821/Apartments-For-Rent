package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
}
