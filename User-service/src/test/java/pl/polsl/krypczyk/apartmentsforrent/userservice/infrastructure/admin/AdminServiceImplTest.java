package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AdminServiceImplTest {

    private static final String VALID_USER_SURNAME = "surname";
    private static final String VALID_USER_PASSWORD = "password";
    private static final String VALID_USER_NAME = "name";
    private static final String VALID_USER_EMAIL = "user@user.com";
    private static final boolean VALID_USER_IS_ACTIVE = true;
    private static final boolean INACTIVE_USER_IS_ACTIVE = false;
    private static final Long INVALID_USER_ID = 12L;

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void deleteDbContent() {
        this.userRepository.deleteAll();
    }

    @Test
    void testGetAllUsersWithNotEmptyUsersListShouldReturnNotEmptyResponse() throws UserAlreadyExistsException {
        //GIVEN
        var user = this.createValidUser();
        this.userService.createUser(user);

        //WHEN
        var response = this.adminService.getAllUsers();

        //THEN
        Assertions.assertFalse(response.isEmpty());
    }

    @Test
    void testGetAllUsersWithEmptyUsersListShouldReturnEmptyResponse() {
        //GIVEN
        this.deleteDbContent();
        //WHEN
        var response = this.adminService.getAllUsers();

        //THEN
        Assertions.assertTrue(response.isEmpty());
    }


    @Test
    void testDeleteUserWithValidUserIdShouldNotThrowUserNotFoundException() throws UserNotFoundException, UserAlreadyExistsException {
        //GIVEN
        var user = this.createValidUser();
        var response = this.userService.createUser(user);
        var userId = response.getId();

        //WHEN
        this.adminService.deleteUser(userId);
        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testDeleteUserWithInvalidUserIdShouldThrowUserNotFoundException() {
        //GIVEN
        this.createValidUser();


        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.adminService.deleteUser(INVALID_USER_ID));
    }

    @Test
    void testChangeUserDetailsWithValidUserIdShouldNotThrowUserNotFoundException() throws UserAlreadyExistsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN
        this.adminService.changeUserDetails(changeUserDetailsRequest, userId);
        //WHEN AND THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdShouldThrowUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var user = this.createValidUser();
        this.userService.createUser(user);
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.adminService.changeUserDetails(changeUserDetailsRequest, INVALID_USER_ID));
    }

    @Test
    void testChangeUserDetailsWithNullUserDetailsShouldThrowInvalidUserException() throws UserAlreadyExistsException {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var createUserResponse = this.userService.createUser(inactiveUser);
        var userId = createUserResponse.getId();

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserDetailsException.class, () ->
                this.adminService.changeUserDetails(null, userId));
    }

    @Test
    void testActivateAccountWithValidUserIdShouldNotThrowUserNotFoundException() throws UserAlreadyExistsException, UserNotFoundException {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var createUserResponse = this.userService.createUser(inactiveUser);
        var userId = createUserResponse.getId();

        //WHEN
        this.adminService.activateAccount(userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testActivateAccountWithInvalidUserIdUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        this.userService.createUser(inactiveUser);

        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () -> this.adminService.activateAccount(0L));
    }

    private CreateUserRequest createValidUser() {
        return CreateUserRequest.builder()
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

}