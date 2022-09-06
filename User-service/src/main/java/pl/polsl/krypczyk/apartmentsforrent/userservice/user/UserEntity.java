package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsEntity;

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

    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "ID")
    private UserDetailsEntity userDetailsEntity;

    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "USER_AUTHORIZATION_ID", referencedColumnName = "ID", nullable = false)
    private UserAuthorizationEntity userAuthorizationEntity;
}
