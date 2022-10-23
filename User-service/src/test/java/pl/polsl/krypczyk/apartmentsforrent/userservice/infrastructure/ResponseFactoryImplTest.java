package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class ResponseFactoryImplTest {

    @Autowired
    private ResponseFactory responseFactory;

    private static final String PASSWORD = "password";
    private static final String ENCRYPTED_PASSWORD = "yCEJkysxD1f438P+d+3cyA==";
    private static final String EMAIL = "email@email.com";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final Boolean IS_ACTIVE = true;
    private static final UUID ACCESS_TOKEN = UUID.randomUUID();
    private static final Long USER_ID = 1L;

    private static final List<String> ROLES = List.of("ROLE_USER");

    @Test
    void testCreateUserDTOWithValidUserEntityShouldReturnExpectedUserDTO() {
        //GIVEN
        var user = validUserEntity();
        var expected = validUserDTO();

        //WHEN
        var actual = this.responseFactory.createUserDTO(user);

        //THEN
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
        Assertions.assertEquals(expected.getIsActive(), actual.getIsActive());
    }

    @Test
    void testCreateChangeUserDetailsResponseWithValidChangeUserDetailsRequestShouldReturnExpectedChangeUserDetailsResponse() {
        //GIVEN
        var changeUserDetailsRequest = validChangeUserDetailsRequest();
        var expected = validChangeUserDetailsResponse();

        //WHEN
        var actual = this.responseFactory.createChangeUserDetailsResponse(changeUserDetailsRequest);

        //THEN
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void testCreateCreateUserResponseWithValidCreateUserRequestShouldReturnExpectedCreateUserResponse() {
        //GIVEN
        var createUserRequest = validCreateUserRequest();
        var expected = validCreateUserResponse();

        //WHEN
        var actual = this.responseFactory.createCreateUserResponse(createUserRequest, validUserAuthorizationEntity(), USER_ID);

        //THEN
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertFalse(Objects.isNull(actual.getAccessToken()));
        Assertions.assertFalse(Objects.isNull(actual.getCreationDate()));
    }

    @Test
    void testCreateLoginUserResponseWithValidDataShouldReturnExpectedLoginResponse() {
        //GIVEN
        var expected = validLoginUserResponse();

        //WHEN
        var actual = this.responseFactory.createLoginUserResponse(ACCESS_TOKEN, EMAIL, List.of(validRoleEntity()), USER_ID);

        //THEN
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
        Assertions.assertEquals(expected.getAccessToken(), actual.getAccessToken());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void testCreateGetUserDetailsResponseShouldReturnExpectedGetUserDetailsResponse() {
        //GIVEN
        var userDetails = validUserDetailsEntity();
        var expected = validGetUserDetailsResponse();

        //WHEN
        var actual = this.responseFactory.createGetUserDetailsResponse(userDetails);

        //THEN
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getIsActive(), actual.getIsActive());
    }

    private RoleEntity validRoleEntity() {
        return new RoleEntity("ROLE_USER");
    }

    private UserAuthorizationEntity validUserAuthorizationEntity() {
        var userAuthorization = new UserAuthorizationEntity();
        userAuthorization.setRoles(List.of(validRoleEntity()));
        userAuthorization.setToken(ACCESS_TOKEN);
        return userAuthorization;
    }

    private UserDetailsEntity validUserDetailsEntity() {
        var userDetails = new UserDetailsEntity();
        userDetails.setPassword(ENCRYPTED_PASSWORD);
        userDetails.setEmail(EMAIL);
        userDetails.setName(NAME);
        userDetails.setIsActive(IS_ACTIVE);
        userDetails.setSurname(SURNAME);
        return userDetails;
    }

    private UserEntity validUserEntity() {
        var user = new UserEntity();
        user.setUserAuthorizationEntity(validUserAuthorizationEntity());
        user.setUserDetailsEntity(validUserDetailsEntity());
        return user;
    }

    private UserDTO validUserDTO() {
        return UserDTO
                .builder()
                .surname(SURNAME)
                .email(EMAIL)
                .isActive(IS_ACTIVE)
                .password(PASSWORD)
                .name(NAME)
                .roles(ROLES)
                .build();
    }

    private ChangeUserDetailsRequest validChangeUserDetailsRequest() {
        return ChangeUserDetailsRequest
                .builder()
                .surname(SURNAME)
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .build();
    }

    private ChangeUserDetailsResponse validChangeUserDetailsResponse() {
        return ChangeUserDetailsResponse
                .builder()
                .surname(SURNAME)
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .build();
    }

    private CreateUserRequest validCreateUserRequest() {
        return CreateUserRequest
                .builder()
                .surname(SURNAME)
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }

    private CreateUserResponse validCreateUserResponse() {
        return CreateUserResponse
                .builder()
                .email(EMAIL)
                .roles(List.of("ROLE_USER"))
                .build();
    }

    private LoginUserResponse validLoginUserResponse() {
        return LoginUserResponse
                .builder()
                .id(USER_ID)
                .accessToken(ACCESS_TOKEN)
                .email(EMAIL)
                .roles(ROLES)
                .build();
    }

    private GetUserDetailsResponse validGetUserDetailsResponse(){
        return GetUserDetailsResponse
                .builder()
                .surname(SURNAME)
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }
}