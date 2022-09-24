package pl.polsl.krypczyk.apartmentsforrent.userservice.domain;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

import java.util.Collection;
import java.util.UUID;

public interface ResponseFactory {
    UserDTO createUserDTO(UserEntity user);

    ChangeUserDetailsResponse createChangeUserDetailsResponse(ChangeUserDetailsRequest changeUserDetailsRequest);

    CreateUserResponse createCreateUserResponse(CreateUserRequest createUserRequest,
                                                UserAuthorizationEntity userAuthorization,
                                                Long userId);

    LoginUserResponse createLoginUserResponse(UUID accessToken,
                                              String email,
                                              Collection<RoleEntity> roles,
                                              Long userId);

    GetUserDetailsResponse createGetUserDetailsResponse(UserDetailsEntity userDetails);

}
