package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import java.time.LocalDateTime;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class UserServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;
    private final LocalDateTime USER_CREATION_DATE = null;
    private final Long INVALID_USER_ID = 12334343L;


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void deleteDbContent() {
        this.userRepository.deleteAll();
    }

    @Test
    void testGetUserDetailsWithValidUserIdShouldReturnExpectedUserDetailsAndNotThrowUserNotFoundException() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var userDetailsDTO = this.createValidUserDetailsDTO();

        //WHEN
        var expected = this.userService.getUserDetails(userId);
        expected.setCreationDate(USER_CREATION_DATE);

        //THEN
        Assertions.assertEquals(expected, userDetailsDTO);
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testGetUserDetailsWithInvalidUserIdShouldThrowUserNotFoundException() {
        //GIVEN
        var userId = INVALID_USER_ID;

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.getUserDetails(userId));
    }

    @Test
    void testChangeUserDetailsWithValidUserDetailsShouldNotThrowUserNotFoundException() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail("test@test2.pl");

        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testChangeUserDetailsWithExistingEmailShouldThrowUserAlreadyExistsException() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testChangeUserDetailsWithValidUserIdShouldNotThrowUserNotFoundException() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail("test@test2.pl");
        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);
        //WHEN AND THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdShouldThrowUserNotFoundException() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createValidUser();
        this.userService.createUser(user);
        var userId = INVALID_USER_ID;
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testInactivateAccountWithValidUserIdShouldNotThrowUserNotFoundAndUnauthorizedException() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();

        //WHEN
        this.userService.inactivateAccount(userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(UnauthorizedUserException::new);
    }

    @Test
    void testInactivateAccountWithInvalidUserIdShouldThrowUserNotFoundException() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createValidUser();
        this.userService.createUser(user);
        var userId = INVALID_USER_ID;

        // WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.inactivateAccount(userId));
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

    private ChangeUserDetailsRequest createValidChangeUserDetailsRequest() {
        return ChangeUserDetailsRequest.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .build();
    }

    private GetUserDetailsResponse createValidUserDetailsDTO() {
        return GetUserDetailsResponse
                .builder()
                .name(VALID_USER_NAME)
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .email(VALID_USER_EMAIL)
                .isActive(VALID_USER_IS_ACTIVE)
                .creationDate(USER_CREATION_DATE)
                .build();
    }
}