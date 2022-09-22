package pl.polsl.krypczyk.apartmentsforrent.gateway.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userdetails.UserDetailsEntity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

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
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "ID", nullable = false)
    private UserDetailsEntity userDetailsEntity;

    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "USER_AUTHORIZATION_ID", referencedColumnName = "ID", nullable = false)
    private UserAuthorizationEntity userAuthorizationEntity;
}
