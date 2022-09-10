package pl.polsl.krypczyk.apartmentsforrent.userservice.user.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity save(RoleEntity roleEntity);
}
