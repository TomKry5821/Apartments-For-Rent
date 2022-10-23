package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ResponseFactoryImpl implements ResponseFactory {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UserDTO createUserDTO(UserEntity user) {
        var userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getUserDetailsEntity().getName())
                .surname(user.getUserDetailsEntity().getSurname())
                .email(user.getUserDetailsEntity().getEmail())
                .password(AES.decrypt(user.getUserDetailsEntity().getPassword()))
                .creationDate(user.getUserDetailsEntity().getCreationDate())
                .isActive(user.getUserDetailsEntity().getIsActive())
                .accessToken(user.getUserAuthorizationEntity().getToken())
                .roles(user.getUserAuthorizationEntity().getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList()))
                .build();

        log.trace("Created user DTO - " + userDTO);
        return userDTO;
    }

    @Override
    public ChangeUserDetailsResponse createChangeUserDetailsResponse(ChangeUserDetailsRequest changeUserDetailsRequest) {
        var changeUserDetailsResponse = this.userMapper.ChangeUserDetailsRequestToChangeUserDetailsResponse(changeUserDetailsRequest);

        log.trace("Created change user details response - " + changeUserDetailsResponse);
        return changeUserDetailsResponse;
    }

    @Override
    public CreateUserResponse createCreateUserResponse(CreateUserRequest createUserRequest, UserAuthorizationEntity userAuthorization, Long userId) {
        var createUserResponse = CreateUserResponse
                .builder()
                .email(createUserRequest.getEmail())
                .accessToken(userAuthorization.getToken())
                .roles(userAuthorization.getRoles()
                        .stream()
                        .map(RoleEntity::getName).collect(Collectors.toList()))
                .id(userId)
                .creationDate(LocalDateTime.now())
                .build();

        log.trace("Created create user Response - " + createUserResponse);
        return createUserResponse;
    }

    @Override
    public LoginUserResponse createLoginUserResponse(UUID accessToken, String email, Collection<RoleEntity> roles, Long userId) {
        var loginUserResponse = LoginUserResponse
                .builder()
                .accessToken(accessToken)
                .email(email)
                .roles(roles.stream()
                        .map(RoleEntity::getName).collect(Collectors.toList()))
                .id(userId)
                .build();

        log.trace("Created login user response - " + loginUserResponse);
        return loginUserResponse;
    }

    @Override
    public GetUserDetailsResponse createGetUserDetailsResponse(UserDetailsEntity userDetails) {
        var getUserDetailsResponse = userMapper.UserDetailsEntityToUserDetailsDTO(userDetails);
        getUserDetailsResponse.setPassword(AES.decrypt(userDetails.getPassword()));

        log.trace("Created get user details response - " + getUserDetailsResponse);
        return getUserDetailsResponse;
    }
}
