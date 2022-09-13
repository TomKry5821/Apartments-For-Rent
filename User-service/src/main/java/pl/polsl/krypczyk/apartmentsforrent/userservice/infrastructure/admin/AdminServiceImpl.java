package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.response.GetAllUsersResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public void deleteUser(Long userId) {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        this.userRepository.delete(user);
    }

    @Override
    public GetAllUsersResponse getAllUsers() {
        var users = this.userRepository.findAll();
        Collection<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(u -> userDTOS.add(this.buildUserDTO(u)));

        var getAllUsersResponse = new GetAllUsersResponse();
        getAllUsersResponse.setUsers(userDTOS);

        return getAllUsersResponse;
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
