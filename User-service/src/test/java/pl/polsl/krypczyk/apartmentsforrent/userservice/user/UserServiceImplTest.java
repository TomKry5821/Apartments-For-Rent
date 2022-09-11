package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.exception.AccountNotActiveException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.ChangeUserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.CreateUserRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;
    private final boolean INACTIVE_USER_IS_ACTIVE = false;
    private final LocalDateTime USER_CREATION_DATE = null;
    private final Long INVALID_USER_ID = 12L;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.userService.deleteDbContent();
    }

    @Test
    void getUserDetailsWithValidUserId() {
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
    void getUserDetailsWithInvalidUserId() {
        //GIVEN
        var userId = INVALID_USER_ID;

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.getUserDetails(userId));
    }

    @Test
    void getUserDetailsWithInactiveUser() {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var response = this.authorizationService.registerNewUser(inactiveUser);
        var userId = response.getId();

        //WHEN AND THEN
        Assertions.assertThrows(AccountNotActiveException.class, () ->
                this.userService.getUserDetails(userId));
    }

    @Test
    void changeUserDetailsWithValidUserDetails() {
        //GIVEN
        var user = this.createValidUser();
        var response = this.authorizationService.registerNewUser(user);
        var userId = response.getId();
        var changeUserDetailsDTO = this.createValidChangeUserDetailsDTO(userId);

        //WHEN
        var expected = this.userService.changeUserDetails(changeUserDetailsDTO);

        //THEN
        Assertions.assertEquals(expected, changeUserDetailsDTO);
        Assertions.assertDoesNotThrow(UserServiceImplTest::new);
    }

    @Test
    void changeUserDetailsWithInvalidUserId() {
        //GIVEN
        var user = this.createValidUser();
        this.authorizationService.registerNewUser(user);
        var userId = INVALID_USER_ID;
        var changeUserDetailsDTO = this.createValidChangeUserDetailsDTO(userId);

        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsDTO));
    }

    @Test
    void changeUserDetailsWithNullUserDetails() {
        //GIVEN
        ChangeUserDetailsDTO changeUserDetailsDTO = null;

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserDetailsException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsDTO));
    }

    @Test
    void changeUserDetailsWithInactiveUser() {
        //GIVEN
        var inactiveUser = this.createInactiveUser();
        var response = this.authorizationService.registerNewUser(inactiveUser);
        var userId = response.getId();
        var changeUserDetailsDTO = this.createValidChangeUserDetailsDTO(userId);

        //WHEN AND THEN
        Assertions.assertThrows(AccountNotActiveException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsDTO));
    }

    private CreateUserRequestDTO createValidUser() {
        return CreateUserRequestDTO.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .isActive(VALID_USER_IS_ACTIVE)
                .build();
    }

    private CreateUserRequestDTO createInactiveUser() {
        return CreateUserRequestDTO.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .isActive(INACTIVE_USER_IS_ACTIVE)
                .build();
    }

    private ChangeUserDetailsDTO createValidChangeUserDetailsDTO(Long userId) {
        return ChangeUserDetailsDTO.builder()
                .surname(VALID_USER_SURNAME)
                .password(VALID_USER_PASSWORD)
                .name(VALID_USER_NAME)
                .email(VALID_USER_EMAIL)
                .isActive(VALID_USER_IS_ACTIVE)
                .userId(userId)
                .build();
    }

    private UserDetailsDTO createValidUserDetailsDTO() {
        return UserDetailsDTO
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