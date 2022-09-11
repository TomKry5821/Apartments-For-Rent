package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.mapper.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoggedInResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoginRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.CreateUserRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public UserCreatedResponseDTO registerNewUser(CreateUserRequestDTO createUserRequestDTO) {
        if (Objects.isNull(createUserRequestDTO)) {
            throw new BadCredentialsException();
        }
        if (this.userAlreadyExists(createUserRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        var role = this.createAndSaveUserRole();
        var userDetails = createAndSaveUserDetails(createUserRequestDTO);
        var userAuthorization = createAndSaveUserAuthorization(role);
        var user = this.createAndSaveUserEntity(userDetails, userAuthorization);

        return new UserCreatedResponseDTO(createUserRequestDTO.getEmail(), userAuthorization.getToken(),
                userAuthorization.getRoles()
                        .stream()
                        .map(RoleEntity::getName).collect(Collectors.toList()),
                user.getId()
        );
    }

    private Boolean userAlreadyExists(String email) {
        return this.userDetailsRepository.existsByEmail(email);
    }

    private RoleEntity createAndSaveUserRole() {
        var role = new RoleEntity("ROLE_USER");
        roleRepository.save(role);
        return role;
    }

    private UserAuthorizationEntity createAndSaveUserAuthorization(RoleEntity roleEntity) {
        var userAuthorization = new UserAuthorizationEntity();

        userAuthorization.setToken(UUID.randomUUID());
        userAuthorization.setRoles(List.of(roleEntity));
        userAuthorizationRepository.save(userAuthorization);
        return userAuthorization;
    }

    private UserDetailsEntity createAndSaveUserDetails(CreateUserRequestDTO createUserRequestDTO) {
        var userDetails = this.userMapper.userDetailsDTOToUserDetailsEntity(createUserRequestDTO);
        userDetails.setCreationDate(LocalDateTime.now());
        userDetailsRepository.save(userDetails);
        return userDetails;
    }

    private UserEntity createAndSaveUserEntity(UserDetailsEntity userDetailsEntity,
                                               UserAuthorizationEntity userAuthorizationEntity) {
        var userEntity = new UserEntity();
        userEntity.setUserDetailsEntity(userDetailsEntity);
        userEntity.setUserAuthorizationEntity(userAuthorizationEntity);
        userRepository.save(userEntity);

        return userEntity;
    }

    @Override
    public UserLoggedInResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        if (Objects.isNull(userLoginRequestDTO)) {
            throw new BadCredentialsException();
        }

        UserDetailsEntity userDetails = retrieveUserDetailsByEmailAndPassword(userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());

        UserEntity user = this.userRepository.findUserEntityByUserDetailsEntity(userDetails);
        var userAuthorization = user.getUserAuthorizationEntity();
        this.updateAccessToken(userAuthorization);

        return new UserLoggedInResponseDTO(userAuthorization.getToken(), userDetails.getEmail(), userAuthorization.getRoles().stream()
                .map(RoleEntity::getName).collect(Collectors.toList()), user.getId());
    }

    private UserDetailsEntity retrieveUserDetailsByEmailAndPassword(String email, String password) {
        var userDetails = this.userDetailsRepository.findUserDetailsEntityByEmailAndPassword(email, password);
        if (Objects.isNull(userDetails)) {
            throw new UserNotFoundException();
        }

        return userDetails;
    }

    private void updateAccessToken(UserAuthorizationEntity userAuthorization) {
        userAuthorization.setToken(UUID.randomUUID());
        this.userAuthorizationRepository.save(userAuthorization);
    }

    @Override
    public void logoutUser(Long userId) {
        var user = this.findUserByUserId(userId);
        var userAuthorization = user.getUserAuthorizationEntity();
        this.resetUserAccessToken(userAuthorization);
    }

    private void resetUserAccessToken(UserAuthorizationEntity userAuthorization) {
        userAuthorization.setToken(UUID.randomUUID());
        userAuthorizationRepository.save(userAuthorization);
    }

    private UserEntity findUserByUserId(Long userId) {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException();
        }

        return user;
    }

    //////////////////////////////////////////////////////////////////////
    ///////////////////////////////FOR TEST PURPOSES//////////////////////
    @Override
    public void deleteDbContent() {
        this.userRepository.deleteAll();
    }
}
