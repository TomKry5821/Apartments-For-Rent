package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.GetUserDetailsResponse;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ResponseFactory responseFactory;
    private final KafkaMessageProducer kafkaMessageProducer;
    private final EntityFactory entityFactory;

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) throws BadCredentialsException, UserAlreadyExistsException {
        log.info("Started creating new user with details - " + createUserRequest);

        if (Objects.isNull(createUserRequest))
            throw new BadCredentialsException();
        if (this.userAlreadyExists(createUserRequest.getEmail()))
            throw new UserAlreadyExistsException();

        var role = this.entityFactory.createUserRoleEntity();
        var userDetails = this.entityFactory.createUserDetailsEntity(createUserRequest);
        var userAuthorization = this.entityFactory.createUserAuthorizationEntity(role);
        var user = this.entityFactory.createUserEntity(userDetails, userAuthorization);

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
