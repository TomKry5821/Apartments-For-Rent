package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class UserServiceImplTest {

    private static final String SURNAME = "surname";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "user@user.com";
    private static final String CHANGED_EMAIL = "user2@user.com";
    private static final boolean IS_ACTIVE = true;
    private static final Long INVALID_USER_ID = 12334343L;
    private static final String UNKNOWN_USER = "Nieznany użytkownik";


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorizationRepository userAuthorizationRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void deleteDbContent() {
        this.userRepository.deleteAll();
    }

    @Test
    void testCreateUserWithValidUserIdShouldReturnExpectedUserDetailsAndNotThrowUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = this.createValidUserRequest();

        //WHEN
        this.userService.createUser(createUserRequest);

        //THEN
        Assertions.assertFalse(this.userRepository.findAll().isEmpty());
        Assertions.assertFalse(this.userDetailsRepository.findAll().isEmpty());
        Assertions.assertFalse(this.userAuthorizationRepository.findAll().isEmpty());
        Assertions.assertFalse(this.roleRepository.findAll().isEmpty());
    }

    @Test
    void testCreateAlreadyExistingUserShouldThrowUserAlreadyExistsException() throws UserAlreadyExistsException {
        //GIVEN
        var createUserRequest = createValidUserRequest();

        //WHEN
        this.userService.createUser(createUserRequest);

        //THEN
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                this.userService.createUser(createUserRequest));
    }

    @Test
    void testGetUserDetailsWithValidUserIdShouldReturnExpectedUserDetailsAndNotThrowUserNotFoundException() throws UserAlreadyExistsException, UserNotFoundException {
        //GIVEN
        var user = this.createValidUserRequest();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();

        //WHEN
        this.userService.getUserDetails(userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testGetUserDetailsWithInvalidUserIdShouldThrowUserNotFoundException() {
        //GIVEN
        //WHEN
        //THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.getUserDetails(INVALID_USER_ID));
    }

    @Test
    void testChangeUserDetailsWithValidUserDetailsShouldNotThrowUserNotFoundAndUserAlreadyExistsException() throws UserAlreadyExistsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUserRequest();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();
        changeUserDetailsRequest.setEmail(CHANGED_EMAIL);

        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(UserAlreadyExistsException::new);
    }

    @Test
    void testChangeUserDetailsWithoutEmailChangeShouldNotThrowUserAlreadyExistsException() throws UserAlreadyExistsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUserRequest();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN
        this.userService.changeUserDetails(changeUserDetailsRequest, userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
        Assertions.assertDoesNotThrow(UserAlreadyExistsException::new);
    }

    @Test
    void testChangeUserDetailsWithValidUserIdShouldNotThrowUserNotFoundException() throws UserAlreadyExistsException, UserNotFoundException, InvalidUserDetailsException {
        //GIVEN
        var user = this.createValidUserRequest();
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
    void testChangeUserDetailsWithInvalidUserIdShouldThrowUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var user = this.createValidUserRequest();
        this.userService.createUser(user);
        var changeUserDetailsRequest = this.createValidChangeUserDetailsRequest();

        //WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.changeUserDetails(changeUserDetailsRequest, INVALID_USER_ID));
    }

    @Test
    void testInactivateAccountWithValidUserIdShouldNotThrowUserNotFoundException() throws UserAlreadyExistsException, UserNotFoundException {
        //GIVEN
        var user = this.createValidUserRequest();
        var createUserResponse = this.userService.createUser(user);
        var userId = createUserResponse.getId();

        //WHEN
        this.userService.inactivateAccount(userId);

        //THEN
        Assertions.assertDoesNotThrow(UserNotFoundException::new);
    }

    @Test
    void testInactivateAccountWithInvalidUserIdShouldThrowUserNotFoundException() throws UserAlreadyExistsException {
        //GIVEN
        var user = this.createValidUserRequest();
        this.userService.createUser(user);

        // WHEN AND THEN
        Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.inactivateAccount(INVALID_USER_ID));
    }

    @Test
    void testGetUsernameWithValidUserIdShouldReturnExpectedUsername() throws UserAlreadyExistsException {
        //GIVEN
        var user = this.createValidUserRequest();
        var response = this.userService.createUser(user);
        var expected = user.getName();

        //WHEN
        var actual = this.userService.getUsername(response.getId());

        //THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetUsernameWithInvalidUserIdShouldReturnExpectedUsername() {
        //GIVEN
        //WHEN
        var actual = this.userService.getUsername(INVALID_USER_ID);

        //THEN
        Assertions.assertEquals(UNKNOWN_USER, actual);
    }

    private CreateUserRequest createValidUserRequest() {
        return CreateUserRequest.builder()
                .surname(SURNAME)
                .password(PASSWORD)
                .name(NAME)
                .email(EMAIL)
                .isActive(IS_ACTIVE)
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