package pl.polsl.krypczyk.apartmentsforrent.gateway.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization.UserAuthorizationEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityByUserAuthorizationEntity(UserAuthorizationEntity userAuthorizationEntity);
}
