package pl.polsl.krypczyk.apartmentsforrent.userservice.domain;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

public interface EntityFactory {

    RoleEntity createUserRoleEntity();

    UserAuthorizationEntity createUserAuthorizationEntity(RoleEntity roleEntity);

    UserDetailsEntity createUserDetailsEntity(CreateUserRequest createUserRequest);

    UserEntity createUserEntity(UserDetailsEntity userDetailsEntity,
                                UserAuthorizationEntity userAuthorizationEntity);
}
