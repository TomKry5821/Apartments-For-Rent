package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.authorization.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
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
public class ResponseFactoryImpl implements ResponseFactory {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UserDTO createUserDTO(UserEntity user) {
        return UserDTO.builder()
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
    }

    @Override
    public ChangeUserDetailsResponse createChangeUserDetailsResponse(ChangeUserDetailsRequest changeUserDetailsRequest) {
        return this.userMapper.ChangeUserDetailsRequestToChangeUserDetailsResponse(changeUserDetailsRequest);

    }

    @Override
    public CreateUserResponse createCreateUserResponse(CreateUserRequest createUserRequest, UserAuthorizationEntity userAuthorization, Long userId) {
        return CreateUserResponse
                .builder()
                .email(createUserRequest.getEmail())
                .accessToken(userAuthorization.getToken())
                .roles(userAuthorization.getRoles()
                        .stream()
                        .map(RoleEntity::getName).collect(Collectors.toList()))
                .id(userId)
                .creationDate(LocalDateTime.now())
                .build();
    }

    @Override
    public LoginUserResponse createLoginUserResponse(UUID accessToken, String email, Collection<RoleEntity> roles, Long userId) {
        return LoginUserResponse
                .builder()
                .accessToken(accessToken)
                .email(email)
                .roles(roles.stream()
                        .map(RoleEntity::getName).collect(Collectors.toList()))
                .id(userId)
                .build();
    }

    @Override
    public GetUserDetailsResponse createGetUserDetailsResponse(UserDetailsEntity userDetails) {
        var getUserDetailsResponse = userMapper.UserDetailsEntityToUserDetailsDTO(userDetails);
        getUserDetailsResponse.setPassword(AES.decrypt(userDetails.getPassword()));

        return getUserDetailsResponse;
    }
}
