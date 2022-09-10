package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoginRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.CreateUserRequestDTO;

@SpringBootTest
class AuthorizationServiceImplTest {

    private final String VALID_USER_SURNAME = "surname";
    private final String VALID_USER_PASSWORD = "password";
    private final String VALID_USER_NAME = "name";
    private final String VALID_USER_EMAIL = "user@user.com";
    private final boolean VALID_USER_IS_ACTIVE = true;

    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.authorizationService.deleteDbContent();
    }

    @Test
    void registerNewUserWithValidUserInformation() {
        //GIVEN
        CreateUserRequestDTO createUserRequestDTO = createValidUser();

        //WHEN
        this.authorizationService.registerNewUser(createUserRequestDTO);

        //THEN
        Assertions.assertDoesNotThrow(BadCredentialsException::new);
    }

    @Test
    void registerAlreadyExistingUser() {
        //GIVEN
        CreateUserRequestDTO createUserRequestDTO = createValidUser();

        //WHEN
        this.authorizationService.registerNewUser(createUserRequestDTO);

        //THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.authorizationService.registerNewUser(createUserRequestDTO));
    }

    @Test
    void registerNewUserWithInvalidNullUserInformation() {
        //GIVEN
        CreateUserRequestDTO createUserRequestDTO = null;

        //WHEN AND THEN
        Assertions.assertThrows(BadCredentialsException.class, () ->
                this.authorizationService.registerNewUser(createUserRequestDTO));
    }

    @Test
    void loginUserWithValidCredentials() {
        //GIVEN
        CreateUserRequestDTO createUserRequestDTO = createValidUser();
        this.authorizationService.registerNewUser(createUserRequestDTO);

        //WHEN
        UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO(VALID_USER_EMAIL, VALID_USER_PASSWORD);
        this.authorizationService.loginUser(userLoginRequestDTO);

        //THEN
        Assertions.assertDoesNotThrow(BadCredentialsException::new);
    }

    @Test
    void loginUserWithNullCredentials() {
        //GIVEN
        CreateUserRequestDTO createUserRequestDTO = createValidUser();
        this.authorizationService.registerNewUser(createUserRequestDTO);

        //WHEN
        UserLoginRequestDTO userLoginRequestDTO = null;

        //THEN
        Assertions.assertThrows(BadCredentialsException.class, () ->
                this.authorizationService.loginUser(userLoginRequestDTO));
    }

    @Test
    void loginUserThatDoesNotExists() {
        //GIVEN
        CreateUserRequestDTO createUserRequestDTO = createValidUser();
        this.authorizationService.registerNewUser(createUserRequestDTO);

        //WHEN
        UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO("wrong@mail.com", "wrongpassword");

        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.authorizationService.loginUser(userLoginRequestDTO));
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

}