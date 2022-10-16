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

    private static final String SURNAME = "surname";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "user@user.com";
    private static final boolean IS_ACTIVE = true;
    private static final String INVALID_EMAIL = "wrong@mail.com";
    private static final String INVALID_PASSWORD = "wrongpassword";
    public static final long INVALID_USER_ID = 0L;

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
    void testLoginUserWithValidCredentialsShouldNotThrowUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = createValidUser();
        this.userService.createUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = validLoginRequest(EMAIL, PASSWORD);
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
        UserLoginRequest userLoginRequest = validLoginRequest(INVALID_EMAIL, INVALID_PASSWORD);

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.authorizationService.loginUser(userLoginRequest));
    }

    @Test
    void testLogoutUserThatExistsShouldThrowUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = this.createValidUser();
        var createUserResponse = this.userService.createUser(createUserRequest);
        var userId = createUserResponse.getId();

        //WHEN
        this.authorizationService.logoutUser(userId);

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
                this.authorizationService.logoutUser(INVALID_USER_ID));
    }

    private CreateUserRequest createValidUser() {
        return pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest.builder()
                .surname(SURNAME)
                .password(PASSWORD)
                .name(NAME)
                .email(EMAIL)
                .isActive(IS_ACTIVE)
                .build();
    }

    private UserLoginRequest validLoginRequest(String email, String password) {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(email);
        userLoginRequest.setPassword(password);
        return userLoginRequest;
    }
}

