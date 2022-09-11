package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;


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

    @OneToOne(orphanRemoval = true, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "ID", nullable = false)
    private UserDetailsEntity userDetailsEntity;

    @OneToOne(orphanRemoval = true, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_AUTHORIZATION_ID", referencedColumnName = "ID", nullable = false)
    private UserAuthorizationEntity userAuthorizationEntity;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<RoleEntity> roles = new ArrayList<>();

}
