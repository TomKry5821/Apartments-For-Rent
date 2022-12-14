package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {

    UserDetailsEntity findUserDetailsEntityByEmailAndPassword(String email, String password);

    Boolean existsByEmail(String email);
}
