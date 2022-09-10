package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity save(UserEntity userEntity);

    UserEntity findUserEntityByUserDetailsEntity(UserDetailsEntity userDetailsEntity);
}
