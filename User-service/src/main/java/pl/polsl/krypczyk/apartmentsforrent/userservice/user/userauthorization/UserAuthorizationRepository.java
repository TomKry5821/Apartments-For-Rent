package pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserEntity;

import java.util.UUID;

@Repository
public interface UserAuthorizationRepository extends JpaRepository<UserAuthorizationEntity, Long> {

    UserAuthorizationEntity save(UserAuthorizationEntity userAuthorizationEntity);
    UserAuthorizationEntity findUserAuthorizationEntityByToken(UUID token);
}
