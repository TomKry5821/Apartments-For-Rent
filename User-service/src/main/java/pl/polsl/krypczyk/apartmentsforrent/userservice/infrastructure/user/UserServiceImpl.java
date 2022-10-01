package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.KafkaMessageProducer;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ResponseFactory responseFactory;
    private final KafkaMessageProducer kafkaMessageProducer;

    @Override
    public GetUserDetailsResponse getUserDetails(Long userId) throws UserNotFoundException, InactiveAccountException {
        log.info("Started retrieving user details with provided id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();
        if (this.isAccountInactive(userDetails))
            throw new InactiveAccountException();

        var getUserDetailsResponse = this.responseFactory.createGetUserDetailsResponse(userDetails);

        log.info("successfully retrieved user details - " + getUserDetailsResponse);
        return getUserDetailsResponse;
    }

    @Override
    public ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) throws UserNotFoundException, InactiveAccountException, UserAlreadyExistsException {
        log.info("Started updating user details with provided id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();
        var userDetails = user.getUserDetailsEntity();

        if (this.isAccountInactive(userDetails))
            throw new InactiveAccountException();

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
    public void inactivateAccount(Long userId) throws UserNotFoundException, InactiveAccountException {
        log.info("Started inactivating account with user id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();
        if (this.isAccountInactive(userDetails))
            throw new InactiveAccountException();

        userDetails.setIsActive(false);
        this.userDetailsRepository.save(userDetails);
        this.kafkaMessageProducer.sendInactivateAnnouncementsMessage("topic", userId);

        log.info("Successfully inactivated account with user id - " + userId);
    }

    private Boolean isAccountInactive(UserDetailsEntity userDetails) {
        return userDetails.getIsActive().equals(false);
    }
}
