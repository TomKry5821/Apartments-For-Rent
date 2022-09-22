package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "USER_AUTHORIZATION")
public class UserAuthorizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN")
    private UUID token;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<RoleEntity> roles = new ArrayList<>();
}
