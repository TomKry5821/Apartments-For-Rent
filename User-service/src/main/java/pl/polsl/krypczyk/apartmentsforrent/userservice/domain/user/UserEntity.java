package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "\"USER\"")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(orphanRemoval = true, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "ID", nullable = false)
    private UserDetailsEntity userDetailsEntity;

    @OneToOne(orphanRemoval = true, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_AUTHORIZATION_ID", referencedColumnName = "ID", nullable = false)
    private UserAuthorizationEntity userAuthorizationEntity;
}
