package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@Transactional
class AdminServiceImplTest {

    private static final String SURNAME = "surname";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "user@user.com";
    private static final boolean IS_ACTIVE = true;
    private static final boolean IS_INACTIVE = false;
    private static final Long INVALID_USER_ID = 12L;

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserAuthorizationRepository userAuthorizationRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        Assertions.assertEquals(this.userRepository.findAll().size(), 1);
        Assertions.assertEquals(this.userAuthorizationRepository.findAll().size(), 1);
        Assertions.assertEquals(this.userDetailsRepository.findAll().size(), 1);
        Assertions.assertEquals(this.roleRepository.findAll().size(), 2);
    }

    @Test
    void testDeleteUserWithInvalidUserIdShouldThrowUserNotFoundException() {
        //GIVEN
        //WHEN
        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.adminService.deleteUser(INVALID_USER_ID));
    }

    @Test
    void testChangeUserDetailsWithValidUserIdShouldNotThrowUserNotFoundOrInvalidUserDetailsException() throws UserAlreadyExistsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUser();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN
        this.adminService.changeUserDetails(changeUserDetailsRequest, userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(InvalidUserDetailsException::new);
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
    void testChangeUserDetailsWithNullUserDetailsShouldThrowInvalidUserDetailsException() throws UserAlreadyExistsException {
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
        Assertions.assertThrows(UserNotFoundException.class, () -> this.adminService.activateAccount(INVALID_USER_ID));
    }

    private CreateUserRequest createValidUser() {
        return CreateUserRequest.builder()
                .surname(SURNAME)
                .password(PASSWORD)
                .name(NAME)
                .email(EMAIL)
                .isActive(IS_ACTIVE)
                .build();
    }

    private CreateUserRequest createInactiveUser() {
        return CreateUserRequest.builder()
                .surname(SURNAME)
                .password(PASSWORD)
                .name(NAME)
                .email(EMAIL)
                .isActive(IS_INACTIVE)
                .build();
    }

    private ChangeUserDetailsRequest createValidChangeUserDetailsRequest() {
        return ChangeUserDetailsRequest.builder()
                .surname(SURNAME)
                .password(PASSWORD)
                .name(NAME)
                .email(EMAIL)
                .build();
    }

}