package pl.polsl.krypczyk.apartmentsforrent.userservice.userauthorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserEntity;

@Repository
public interface UserAuthorizationRepository extends JpaRepository<UserAuthorizationEntity, Long> {

    UserAuthorizationEntity save(UserAuthorizationEntity userAuthorizationEntity);

    UserAuthorizationEntity findUserAuthorizationEntityByUserEntity(UserEntity userEntity);
}
