package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

@Getter
@Setter
@RequiredArgsConstructor
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
