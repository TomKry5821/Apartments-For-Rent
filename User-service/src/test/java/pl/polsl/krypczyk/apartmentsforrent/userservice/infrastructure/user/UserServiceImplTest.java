package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
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

    @AfterEach
    void deleteDbContent() {
        this.userService.deleteDbContent();
    }

    @Test
    void testGetUserDetails_WithValidUserId() {
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();
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
    void testGetUserDetails_WithInactiveUser() {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var response = this.authorizationService.registerNewUser(inactiveUser);
        var userId = response.getId();

        //WHEN AND THEN
        Assertions.assertThrows(InactiveAccountException.class, () ->
                this.userService.getUserDetails(userId));
    }

    @Test
    void testChangeUserDetails_WithValidUserDetails() {
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();
        var changeUserDetailsResponse = this.createValidChangeUserDetailsResponse();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail("test@test2.pl");

        //WHEN
        var expected = this.userService.changeUserDetails(changeUserDetailsRequest, userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testChangeUserDetails_WithExistingEmail(){
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();
        this.createValidChangeUserDetailsResponse();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testChangeUserDetails_WithValidUserId() {
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail("test@test2.pl");
        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);
        //WHEN AND THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(InactiveAccountException::new);
    }

    @Test
    void testChangeUserDetails_WithInvalidUserId() {
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
    void testChangeUserDetails_WithNullUserDetails() {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var response = this.authorizationService.registerNewUser(inactiveUser);
        var userId = response.getId();
        ChangeUserDetailsRequest changeUserDetailsRequest = null;

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserDetailsException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testChangeUserDetails_WithInactiveUser() {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var response = this.authorizationService.registerNewUser(inactiveUser);
        var userId = response.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(InactiveAccountException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, userId));
    }

    @Test
    void testInactivateAccount_WithValidUserId() {
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();

        //WHEN
        this.userService.inactivateAccount(userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(UnauthorizedUserException::new);
    }

    @Test
    void testInactivateAccount_WithInvalidUserId() {
        //GIVEN
        var user = this.createValidUser();
        this.authorizationService.registerNewUser(user);
        var userId = INVALID_USER_ID;

        // WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.inactivateAccount(userId));
    }

    @Test
    void testInactivate_InactiveAccount() {
        //GIVEN
        var user = this.createInactiveUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();

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

    private ChangeUserDetailsResponse createValidChangeUserDetailsResponse() {
        return ChangeUserDetailsResponse.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
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