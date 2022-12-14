package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityByUserDetailsEntity(UserDetailsEntity userDetailsEntity);

    UserEntity findUserEntityById(Long id);
}
