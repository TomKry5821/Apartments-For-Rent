package pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {

    UserDetailsEntity save(UserDetailsEntity userDetailsEntity);

    UserDetailsEntity findUserDetailsEntityByEmailAndPassword(String email, String password);
}
