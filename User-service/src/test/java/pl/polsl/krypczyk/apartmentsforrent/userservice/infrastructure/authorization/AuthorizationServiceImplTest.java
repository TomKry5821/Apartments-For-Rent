package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;

@SpringBootTest
class AuthorizationServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;

    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.authorizationService.deleteDbContent();
    }

    @Test
    void registerNewUserWithValidUserInformation() {
        //GIVEN
        CreateUserRequest createUserRequest = createValidUser();

        //WHEN
        this.authorizationService.registerNewUser(createUserRequest);

        //THEN
        Assertions.assertDoesNotThrow(BadCredentialsException::new);
    }

    @Test
    void registerAlreadyExistingUser() {
        //GIVEN
        CreateUserRequest createUserRequest = createValidUser();

        //WHEN
        this.authorizationService.registerNewUser(createUserRequest);

        //THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.authorizationService.registerNewUser(createUserRequest));
    }

    @Test
    void registerNewUserWithInvalidNullUserInformation() {
        //GIVEN
        CreateUserRequest createUserRequest = null;

        //WHEN AND THEN
        Assertions.assertThrows(BadCredentialsException.class, () ->
                this.authorizationService.registerNewUser(createUserRequest));
    }

    @Test
    void loginUserWithValidCredentials() {
        //GIVEN
        CreateUserRequest createUserRequest = createValidUser();
        this.authorizationService.registerNewUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(VALID_USER_EMAIL);
        userLoginRequest.setPassword(VALID_USER_PASSWORD);
        this.authorizationService.loginUser(userLoginRequest);

        //THEN
        Assertions.assertDoesNotThrow(BadCredentialsException::new);
    }

    @Test
    void loginUserWithNullCredentials() {
        //GIVEN
        CreateUserRequest createUserRequest = createValidUser();
        this.authorizationService.registerNewUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = null;

        //THEN
        Assertions.assertThrows(BadCredentialsException.class, () ->
                this.authorizationService.loginUser(userLoginRequest));
    }

    @Test
    void loginUserThatDoesNotExists() {
        //GIVEN
        CreateUserRequest createUserRequest = createValidUser();
        this.authorizationService.registerNewUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("wrong@mail.com");
        userLoginRequest.setPassword("wrongpassword");

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.authorizationService.loginUser(userLoginRequest));
    }

    @Test
    void logoutUserThatExists() {
        //GIVEN
        CreateUserRequest createUserRequest = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(createUserRequest);
        var id = createUserResponse.getId();
        //WHEN
        this.authorizationService.logoutUser(id);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void logoutUserThatDoesNotExists() {
        //GIVEN
        CreateUserRequest createUserRequest = this.createValidUser();
        this.authorizationService.registerNewUser(createUserRequest);

        // WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.authorizationService.logoutUser(0L));
    }

    private CreateUserRequest createValidUser() {
        return pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .isActive(VALID_USER_IS_ACTIVE)
                .build();
    }
}

