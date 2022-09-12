package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class AdminServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;
    private final boolean INACTIVE_USER_IS_ACTIVE = false;
    private final LocalDateTime USER_CREATION_DATE = null;
    private final Long INVALID_USER_ID = 12L;

    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.adminService.deleteDbContent();
    }


    @Test
    void deleteUserWithValidUserId() {
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();

        //WHEN
        this.adminService.deleteUser(userId);
        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void deleteUserWithInvalidUserId() {
        //GIVEN
        this.createValidUser();
        var userId = INVALID_USER_ID;


        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.adminService.deleteUser(userId));
    }

    @Test
    void getAllUsersWithNotEmptyUsersList(){
        //GIVEN
        var user = this.createValidUser();
        this.authorizationService.registerNewUser(user);

        //WHEN
        var response = this.adminService.getAllUsers();

        //THEN
        Assertions.assertFalse(response.getUsers().isEmpty());
    }

    @Test
    void getAllUsersWithEmptyUsersList(){
        //GIVEN
        this.deleteDbContent();
        //WHEN
        var response = this.adminService.getAllUsers();

        //THEN
        Assertions.assertTrue(response.getUsers().isEmpty());    }

    private CreateUserRequest createValidUser() {
        return CreateUserRequest.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .isActive(VALID_USER_IS_ACTIVE)
                .build();
    }

}