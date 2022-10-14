package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.KafkaMessageProducer;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final KafkaMessageProducer kafkaMessageProducer;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ResponseFactory responseFactory;

    @Override
    public Collection<UserDTO> getAllUsers() {
        log.info("Started retrieving all users");

        var users = this.userRepository.findAll();
        Collection<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(u -> userDTOS.add(this.responseFactory.createUserDTO(u)));

        log.info("Successfully retrieved all users");
        return userDTOS;
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        log.info("Started deleting user with id - " + userId);
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        CompletableFuture.runAsync(() -> this.kafkaMessageProducer.sendDeleteAnnouncementMessage(userId));
        CompletableFuture.runAsync(() -> this.kafkaMessageProducer.sendDeleteObservedAnnouncementMessage(userId));

        this.userRepository.delete(user);
        log.info("Started deleting user with id - " + userId);
    }

    @Override
    public ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) throws InvalidUserDetailsException, UserNotFoundException {
        log.info("Started updating user details with user id - " + userId);
        if (Objects.isNull(changeUserDetailsRequest) || Objects.isNull(userId) || userId < 1)
            throw new InvalidUserDetailsException();

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();

        this.changeAndSaveUserDetails(userDetails, changeUserDetailsRequest);
        var changeUserDetailsResponse = this.responseFactory.createChangeUserDetailsResponse(changeUserDetailsRequest);

        log.info("Successfully updated user - " + changeUserDetailsResponse);
        return changeUserDetailsResponse;
    }

    private void changeAndSaveUserDetails(UserDetailsEntity userDetailsEntity, ChangeUserDetailsRequest changeUserDetailsRequest) {
        var name = changeUserDetailsRequest.getName();
        if (!Objects.isNull(name))
            userDetailsEntity.setName(name);
        var surname = changeUserDetailsRequest.getSurname();
        if (!Objects.isNull(surname))
            userDetailsEntity.setSurname(surname);
        var email = changeUserDetailsRequest.getEmail();
        if (!Objects.isNull(email))
            userDetailsEntity.setEmail(email);
        var password = changeUserDetailsRequest.getPassword();
        if (!Objects.isNull(password)) {
            password = AES.encrypt(password);
            userDetailsEntity.setPassword(password);
        }

        this.userDetailsRepository.save(userDetailsEntity);
    }

    @Override
    public void activateAccount(Long userId) throws UserNotFoundException {
        log.info("Started activating account with user id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();

        userDetails.setIsActive(true);
        this.userDetailsRepository.save(userDetails);

        log.info("Successfully activated account with user id - " + userId);
    }
}
