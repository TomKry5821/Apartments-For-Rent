package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.authorization;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

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

    public CreateUserResponse registerNewUser(CreateUserRequest createUserRequest) throws BadCredentialsException, UserAlreadyExistsException {
        if (Objects.isNull(createUserRequest))
            throw new BadCredentialsException();
        if (this.userAlreadyExists(createUserRequest.getEmail()))
            throw new UserAlreadyExistsException();

        var role = this.createAndSaveUserRole();
        var userDetails = createAndSaveUserDetails(createUserRequest);
        var userAuthorization = createAndSaveUserAuthorization(role);
        var user = this.createAndSaveUserEntity(userDetails, userAuthorization);

        return new CreateUserResponse(createUserRequest.getEmail(), userAuthorization.getToken(),
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
        this.roleRepository.save(role);
        return role;
    }

    private UserAuthorizationEntity createAndSaveUserAuthorization(RoleEntity roleEntity) {
        var userAuthorization = new UserAuthorizationEntity();

        userAuthorization.setToken(UUID.randomUUID());
        userAuthorization.setRoles(List.of(roleEntity));
        this.userAuthorizationRepository.save(userAuthorization);
        return userAuthorization;
    }

    private UserDetailsEntity createAndSaveUserDetails(CreateUserRequest createUserRequest) {
        var userDetails = this.userMapper.userDetailsDTOToUserDetailsEntity(createUserRequest);
        userDetails.setCreationDate(LocalDateTime.now());
        userDetails.setPassword(AES.encrypt(userDetails.getPassword()));
        this.userDetailsRepository.save(userDetails);
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
    public LoginUserResponse loginUser(UserLoginRequest userLoginRequest) throws BadCredentialsException, InactiveAccountException, UserNotFoundException {
        if (Objects.isNull(userLoginRequest))
            throw new BadCredentialsException();

        userLoginRequest.setPassword(AES.encrypt(userLoginRequest.getPassword()));
        var userDetails = retrieveUserDetailsByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        if (this.isAccountInactive(userDetails))
            throw new InactiveAccountException();

        var user = this.userRepository.findUserEntityByUserDetailsEntity(userDetails);
        var userAuthorization = user.getUserAuthorizationEntity();
        this.updateAccessToken(userAuthorization);

        return new LoginUserResponse(userAuthorization.getToken(), userDetails.getEmail(), userAuthorization.getRoles().stream()
                .map(RoleEntity::getName).collect(Collectors.toList()), user.getId());
    }

    private UserDetailsEntity retrieveUserDetailsByEmailAndPassword(String email, String password) throws UserNotFoundException {
        var userDetails = this.userDetailsRepository.findUserDetailsEntityByEmailAndPassword(email, password);
        if (Objects.isNull(userDetails))
            throw new UserNotFoundException();

        return userDetails;
    }

    private void updateAccessToken(UserAuthorizationEntity userAuthorization) {
        userAuthorization.setToken(UUID.randomUUID());
        this.userAuthorizationRepository.save(userAuthorization);
    }

    @Override
    public void logoutUser(Long userId) throws InactiveAccountException, UserNotFoundException {
        var user = this.findUserByUserId(userId);
        var userDetails = user.getUserDetailsEntity();
        if (this.isAccountInactive(userDetails))
            throw new InactiveAccountException();
        var userAuthorization = user.getUserAuthorizationEntity();
        this.resetUserAccessToken(userAuthorization);
    }

    private Boolean isAccountInactive(UserDetailsEntity userDetails) {
        return userDetails.getIsActive().equals(false);
    }

    private void resetUserAccessToken(UserAuthorizationEntity userAuthorization) {
        userAuthorization.setToken(UUID.randomUUID());
        this.userAuthorizationRepository.save(userAuthorization);
    }

    private UserEntity findUserByUserId(Long userId) throws UserNotFoundException {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        return user;
    }

    @Override
    public void authorizeUser(Long userId, Long requesterId) throws UnauthorizedUserException {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user) || !userId.equals(requesterId))
            throw new UnauthorizedUserException();
    }

    @Override
    public void authorizeAdmin(Long requesterId) throws UnauthorizedUserException {
        var user = this.userRepository.findUserEntityById(requesterId);
        if (user.getUserAuthorizationEntity().getRoles()
                .stream()
                .noneMatch(r -> r.getName().equals("ROLE_ADMIN")))
            throw new UnauthorizedUserException();
    }
}
