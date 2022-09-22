package pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAuthorizationRepository extends JpaRepository<UserAuthorizationEntity, Long> {

    UserAuthorizationEntity getUserAuthorizationEntityByToken(UUID token);
}
