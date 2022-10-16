package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

import java.util.List;
import java.util.Objects;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class EntityFactoryImplTest {

    private static final String PASSWORD = "password";
    private static final String ENCRYPTED_PASSWORD = "yCEJkysxD1f438P+d+3cyA==";
    private static final String EMAIL = "email@email.com";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final Boolean IS_ACTIVE = true;

    @Autowired
    private EntityFactory entityFactory;

    @Test
    void testCreateUserRoleEntityShouldReturnExpectedRoleEntity() {
        //GIVEN
        var expected = validRoleEntity();

        //WHEN
        var actual = this.entityFactory.createUserRoleEntity();

        //THEN
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    @Transactional
    void testCreateUserAuthorizationEntityShouldReturnExpectedUserAuthorizationEntity() {
        //GIVEN
        var role = validRoleEntity();
        var expected = validUserAuthorizationEntity();

        //WHEN
        var actual = this.entityFactory.createUserAuthorizationEntity(role);

        //THEN
        Assertions.assertEquals(expected.getRoles().stream().toList().get(0).getName(), actual.getRoles().stream().toList().get(0).getName());
        Assertions.assertFalse(Objects.isNull(actual.getToken()));
    }

    @Test
    void testCreateUserDetailsEntityWithValidUserDetailsRequestShouldReturnExpectedUserDetailsEntity() {
        //GIVEN
        var createUserRequest = validCreateUserRequest();
        var expected = validUserDetailsEntity();

        //WHEN
        var actual = this.entityFactory.createUserDetailsEntity(createUserRequest);

        //THEN
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(ENCRYPTED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getIsActive(), actual.getIsActive());
        Assertions.assertFalse(Objects.isNull(actual.getCreationDate()));
    }

    @Test
    @Transactional
    void testCreateUserEntityWithValidUserDetailsAndUserAuthorizationEntityShouldReturnExpectedUserEntity() {
        //GIVEN
        var userAuthorization = this.entityFactory.createUserAuthorizationEntity(validRoleEntity());
        var userDetails = this.entityFactory.createUserDetailsEntity(validCreateUserRequest());
        var expected = validUserEntity();

        //WHEN
        var actual = this.entityFactory.createUserEntity(userDetails, userAuthorization);

        //THEN
        Assertions.assertEquals(expected.getUserAuthorizationEntity().getRoles().stream().toList().get(0).getName(), actual.getUserAuthorizationEntity().getRoles().stream().toList().get(0).getName());
        Assertions.assertEquals(expected.getUserDetailsEntity().getIsActive(), actual.getUserDetailsEntity().getIsActive());
        Assertions.assertEquals(expected.getUserDetailsEntity().getEmail(), actual.getUserDetailsEntity().getEmail());
        Assertions.assertEquals(ENCRYPTED_PASSWORD, actual.getUserDetailsEntity().getPassword());
        Assertions.assertEquals(expected.getUserDetailsEntity().getName(), actual.getUserDetailsEntity().getName());
        Assertions.assertEquals(expected.getUserDetailsEntity().getSurname(), actual.getUserDetailsEntity().getSurname());
    }

    private RoleEntity validRoleEntity() {
        return new RoleEntity("ROLE_USER");
    }

    private UserAuthorizationEntity validUserAuthorizationEntity() {
        var userAuthorization = new UserAuthorizationEntity();
        userAuthorization.setRoles(List.of(validRoleEntity()));
        return userAuthorization;
    }

    private UserDetailsEntity validUserDetailsEntity() {
        var userDetails = new UserDetailsEntity();
        userDetails.setPassword(PASSWORD);
        userDetails.setEmail(EMAIL);
        userDetails.setName(NAME);
        userDetails.setIsActive(IS_ACTIVE);
        userDetails.setSurname(SURNAME);
        return userDetails;
    }

    private CreateUserRequest validCreateUserRequest() {
        return CreateUserRequest
                .builder()
                .surname(SURNAME)
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .build();
    }

    private UserEntity validUserEntity() {
        var user = new UserEntity();
        user.setUserAuthorizationEntity(validUserAuthorizationEntity());
        user.setUserDetailsEntity(validUserDetailsEntity());
        return user;
    }
}