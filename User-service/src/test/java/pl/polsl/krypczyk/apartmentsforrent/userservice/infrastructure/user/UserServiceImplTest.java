package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import java.time.LocalDateTime;

@SpringBootTest
class UserServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;
    private final boolean INACTIVE_USER_IS_ACTIVE = false;
    private final LocalDateTime USER_CREATION_DATE = null;
    private final Long INVALID_USER_ID = 12334343L;


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
    void testGetUserDetails_WithValidUserId() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException, InactiveAccountException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(user);
        var userId = createUserResponse.getId();
        var userDetailsDTO = this.createValidUserDetailsDTO();

        //WHEN
        var expected = this.userService.getUserDetails(userId);
        expected.setCreationDate(USER_CREATION_DATE);

        //THEN
        Assertions.assertEquals(expected, userDetailsDTO);
        Assertions.assertDoesNotThrow(UserServiceImplTest::new);
    }

    @Test
    void testGetUserDetails_WithInvalidUserId() {
        //GIVEN
        var userId = INVALID_USER_ID;

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.getUserDetails(userId));
    }

    @Test
    void testGetUserDetails_WithInactiveUser() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var createUserResponse = this.authorizationService.registerNewUser(inactiveUser);
        var userId = createUserResponse.getId();

        //WHEN AND THEN
        Assertions.assertThrows(InactiveAccountException.class, () ->
                this.userService.getUserDetails(userId));
    }

    @Test
    void testChangeUserDetails_WithValidUserDetails() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException, InactiveAccountException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail("test@test2.pl");

        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testChangeUserDetails_WithExistingEmail() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testChangeUserDetails_WithValidUserId() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException, InactiveAccountException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail("test@test2.pl");
        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);
        //WHEN AND THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(InactiveAccountException::new);
    }

    @Test
    void testChangeUserDetails_WithInvalidUserId() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createValidUser();
        this.authorizationService.registerNewUser(user);
        var userId = INVALID_USER_ID;
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testChangeUserDetails_WithInactiveUser() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var createUserResponse = this.authorizationService.registerNewUser(inactiveUser);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(InactiveAccountException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testInactivateAccount_WithValidUserId() throws UserAlreadyExistsException, BadCredentialsException, UserNotFoundException, InactiveAccountException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.authorizationService.registerNewUser(user);
        var userId = createUserResponse.getId();

        //WHEN
        this.userService.inactivateAccount(userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(UnauthorizedUserException::new);
    }

    @Test
    void testInactivateAccount_WithInvalidUserId() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createValidUser();
        this.authorizationService.registerNewUser(user);
        var userId = INVALID_USER_ID;

        // WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.inactivateAccount(userId));
    }

    @Test
    void testInactivate_InactiveAccount() throws UserAlreadyExistsException, BadCredentialsException {
        //GIVEN
        var user = this.createInactiveUser();
        var createUserResponse = this.authorizationService.registerNewUser(user);
        var userId = createUserResponse.getId();

        // WHEN AND THEN
        Assertions.assertThrows(InactiveAccountException.class, () ->
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

    private CreateUserRequest createInactiveUser() {
        return pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .isActive(INACTIVE_USER_IS_ACTIVE)
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