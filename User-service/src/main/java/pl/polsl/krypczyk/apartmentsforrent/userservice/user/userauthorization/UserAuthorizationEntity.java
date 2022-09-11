package pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.role.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "USER_AUTHORIZATION")
public class UserAuthorizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN", nullable = true)
    private UUID token;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<RoleEntity> roles = new ArrayList<>();
}
