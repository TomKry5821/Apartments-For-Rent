package pl.polsl.krypczyk.apartmentsforrent.userservice.userauthorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorizationRepository extends JpaRepository<UserAuthorizationEntity, Long> {

    UserAuthorizationEntity save(UserAuthorizationEntity userAuthorizationEntity);
}
