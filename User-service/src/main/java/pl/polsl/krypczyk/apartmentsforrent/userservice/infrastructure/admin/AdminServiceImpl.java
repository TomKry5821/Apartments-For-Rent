package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.response.GetAllUsersResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
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
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public GetAllUsersResponse getAllUsers() {
        var users = this.userRepository.findAll();
        Collection<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(u -> userDTOS.add(this.buildUserDTO(u)));

        var getAllUsersResponse = new GetAllUsersResponse();
        getAllUsersResponse.setUsers(userDTOS);

        return getAllUsersResponse;
    }

    @Override
    public void deleteUser(Long userId) {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        this.userRepository.delete(user);
    }

    @Override
    public ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) {
        if (Objects.isNull(changeUserDetailsRequest) || Objects.isNull(userId) || userId < 1)
            throw new InvalidUserDetailsException();

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();
        var userDetails = user.getUserDetailsEntity();

        if (this.isAccountActive(userDetails))
            throw new InactiveAccountException();

        this.changeAndSaveUserDetails(userDetails, changeUserDetailsRequest);
        return this.userMapper.ChangeUserDetailsRequestToChangeUserDetailsResponse(changeUserDetailsRequest);
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
        if (!Objects.isNull(password))
            userDetailsEntity.setPassword(password);

        this.userDetailsRepository.save(userDetailsEntity);
    }

    private Boolean isAccountActive(UserDetailsEntity userDetails) {
        return userDetails.getIsActive().equals(false);
    }

    private UserDTO buildUserDTO(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getUserDetailsEntity().getName())
                .surname(user.getUserDetailsEntity().getSurname())
                .email(user.getUserDetailsEntity().getEmail())
                .password(user.getUserDetailsEntity().getPassword())
                .creationDate(user.getUserDetailsEntity().getCreationDate())
                .isActive(user.getUserDetailsEntity().getIsActive())
                .accessToken(user.getUserAuthorizationEntity().getToken())
                .roles(user.getUserAuthorizationEntity().getRoles()
                        .stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList()))
                .build();
    }

    ////////////////////////////////////////////////
    /////////// FOR TESTS PURPOSE //////////////////
    public void deleteDbContent() {
        this.userRepository.deleteAll();
    }

}
