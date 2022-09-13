package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDetailsRepository extends JpaRepository<AddressDetailsEntity, Long> {
}
