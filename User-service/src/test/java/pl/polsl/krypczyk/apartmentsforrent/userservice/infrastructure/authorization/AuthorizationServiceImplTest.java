package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AuthorizationServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void deleteDbContent() {
        this.userRepository.deleteAll();
    }

    @Test
    void testRegisterNewUser_WithValidUserInformation() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var createUserRequest = createValidUser();

        //WHEN
        this.authorizationService.registerNewUser(createUserRequest);

        //THEN
        Assertions.assertDoesNotThrow(BadCredentialsException::new);
    }

    @Test
    void testRegister_AlreadyExistingUser() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var createUserRequest = createValidUser();

        //WHEN
        this.authorizationService.registerNewUser(createUserRequest);

        //THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.authorizationService.registerNewUser(createUserRequest));
    }

    @Test
    void testRegisterNewUser_WithInvalidNullUserInformation() {
        //GIVEN
        CreateUserRequest createUserRequest = null;

        //WHEN AND THEN
        Assertions.assertThrows(BadCredentialsException.class, () ->
                this.authorizationService.registerNewUser(createUserRequest));
    }

    @Test
    void testLoginUser_WithValidCredentials() throws UserNotFoundException, InactiveAccountException, BadCredentialsException, UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = createValidUser();
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
    void testLoginUser_WithNullCredentials() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var createUserRequest = createValidUser();
        this.authorizationService.registerNewUser(createUserRequest);

        //WHEN
        UserLoginRequest userLoginRequest = null;

        //THEN
        Assertions.assertThrows(BadCredentialsException.class, () ->
                this.authorizationService.loginUser(userLoginRequest));
    }

    @Test
    void testLoginUser_ThatDoesNotExists() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var createUserRequest = createValidUser();
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
    void testLogoutUser_ThatExists() throws UserNotFoundException, InactiveAccountException, UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var createUserRequest = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(createUserRequest);
        var id = createUserResponse.getId();
        //WHEN
        this.authorizationService.logoutUser(id);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testLogoutUser_ThatDoesNotExists() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var createUserRequest = this.createValidUser();
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

