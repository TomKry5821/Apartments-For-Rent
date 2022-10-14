package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AuthorizationServiceImplTest {

    private static final String VALID_USER_SURNAME = "surname";
    private static final String VALID_USER_PASSWORD = "password";
    private static final String VALID_USER_NAME = "name";
    private static final String VALID_USER_EMAIL = "user@user.com";
    private static final boolean VALID_USER_IS_ACTIVE = true;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void deleteDbContent() {
        this.userRepository.deleteAll();
    }

    @Test
    void testRegisterAlreadyExistingUserShouldThrowUserAlreadyExistsException() throws UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = createValidUser();

        //WHEN
        this.userService.createUser(createUserRequest);

        //THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.userService.createUser(createUserRequest));
    }

    @Test
    void testLoginUserWithValidCredentialsShouldNotThrowUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = createValidUser();
        this.userService.createUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(VALID_USER_EMAIL);
        userLoginRequest.setPassword(VALID_USER_PASSWORD);
        this.authorizationService.loginUser(userLoginRequest);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testLoginUserThatDoesNotExistsShouldThrowUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = createValidUser();
        this.userService.createUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("wrong@mail.com");
        userLoginRequest.setPassword("wrongpassword");

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.authorizationService.loginUser(userLoginRequest));
    }

    @Test
    void testLogoutUserThatExistsShouldThrowUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = this.createValidUser();
        var createUserResponse = this.userService.createUser(createUserRequest);
        var id = createUserResponse.getId();

        //WHEN
        this.authorizationService.logoutUser(id);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testLogoutUserThatDoesNotExistsShouldThrowUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = this.createValidUser();
        this.userService.createUser(createUserRequest);

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

