package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.KafkaMessageProducer;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final RoleRepository roleRepository;
    private final ResponseFactory responseFactory;
    private final KafkaMessageProducer kafkaMessageProducer;
    private final EntityFactory entityFactory;

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) throws UserAlreadyExistsException {
        log.info("Started creating new user with details - " + createUserRequest);

        if (this.userAlreadyExists(createUserRequest.getEmail()))
            throw new UserAlreadyExistsException();

        var role = this.entityFactory.createUserRoleEntity();
        this.roleRepository.save(role);
        var userDetails = this.entityFactory.createUserDetailsEntity(createUserRequest);
        this.userDetailsRepository.save(userDetails);
        var userAuthorization = this.entityFactory.createUserAuthorizationEntity(role);
        this.userAuthorizationRepository.save(userAuthorization);
        var user = this.entityFactory.createUserEntity(userDetails, userAuthorization);
        this.userRepository.save(user);

        var createUserResponse = this.responseFactory.createCreateUserResponse(createUserRequest, userAuthorization, user.getId());

        log.info("Successfully created user - " + createUserResponse);
        return createUserResponse;
    }

    private Boolean userAlreadyExists(String email) {
        return this.userDetailsRepository.existsByEmail(email);
    }

    @Override
    public GetUserDetailsResponse getUserDetails(Long userId) throws UserNotFoundException {
        log.info("Started retrieving user details with provided id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();

        var getUserDetailsResponse = this.responseFactory.createGetUserDetailsResponse(userDetails);

        log.info("successfully retrieved user details - " + getUserDetailsResponse);
        return getUserDetailsResponse;
    }

    @Override
    public ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) throws UserNotFoundException, UserAlreadyExistsException {
        log.info("Started updating user details with provided id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();
        var userDetails = user.getUserDetailsEntity();

        this.changeAndSaveUserDetails(userDetails, changeUserDetailsRequest);
        var changeUserDetailsResponse = this.responseFactory.createChangeUserDetailsResponse(changeUserDetailsRequest);

        log.info("Successfully updated user data - " + changeUserDetailsResponse);
        return changeUserDetailsResponse;
    }

    private void changeAndSaveUserDetails(UserDetailsEntity userDetailsEntity, ChangeUserDetailsRequest changeUserDetailsRequest) throws UserAlreadyExistsException {
        var name = changeUserDetailsRequest.getName();
        if (!Objects.isNull(name))
            userDetailsEntity.setName(name);
        var surname = changeUserDetailsRequest.getSurname();
        if (!Objects.isNull(surname))
            userDetailsEntity.setSurname(surname);
        var email = changeUserDetailsRequest.getEmail();
        if (!Objects.isNull(email)) {
            if (this.userDetailsRepository.existsByEmail(email))
                throw new UserAlreadyExistsException();
            userDetailsEntity.setEmail(email);
        }
        var password = changeUserDetailsRequest.getPassword();
        if (!Objects.isNull(password)) {
            password = AES.encrypt(password);
            userDetailsEntity.setPassword(password);
        }

        this.userDetailsRepository.save(userDetailsEntity);
    }

    @Override
    public void inactivateAccount(Long userId) throws UserNotFoundException {
        log.info("Started inactivating account with user id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();
        userDetails.setIsActive(false);
        this.userDetailsRepository.save(userDetails);

        CompletableFuture.runAsync(() -> this.kafkaMessageProducer.sendInactivateAnnouncementsMessage(userId));
        CompletableFuture.runAsync(() -> this.kafkaMessageProducer.sendDeleteObservedAnnouncementMessage(userId));

        log.info("Successfully inactivated account with user id - " + userId);
    }
}
