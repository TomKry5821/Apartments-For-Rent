package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public Collection<UserDTO> getAllUsers() {
        log.info("Started retrieving all users");

        var users = this.userRepository.findAll();
        Collection<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(u -> userDTOS.add(this.buildUserDTO(u)));

        log.info("Successfully retrieved all users");
        return userDTOS;
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        log.info("Started deleting user with id - " + userId);
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

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
        var changeUserDetailsResponse = this.userMapper.ChangeUserDetailsRequestToChangeUserDetailsResponse(changeUserDetailsRequest);

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

    private UserDTO buildUserDTO(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getUserDetailsEntity().getName())
                .surname(user.getUserDetailsEntity().getSurname())
                .email(user.getUserDetailsEntity().getEmail())
                .password(AES.decrypt(user.getUserDetailsEntity().getPassword()))
                .creationDate(user.getUserDetailsEntity().getCreationDate())
                .isActive(user.getUserDetailsEntity().getIsActive())
                .accessToken(user.getUserAuthorizationEntity().getToken())
                .roles(user.getUserAuthorizationEntity().getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
