package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final ResponseFactory responseFactory;
    private final EntityFactory entityFactory;


    @Override
    public CreateUserResponse registerNewUser(CreateUserRequest createUserRequest) throws BadCredentialsException, UserAlreadyExistsException {
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
    public LoginUserResponse loginUser(UserLoginRequest userLoginRequest) throws BadCredentialsException, UserNotFoundException {
        log.info("Started logging user with details -" + userLoginRequest);
        if (Objects.isNull(userLoginRequest))
            throw new BadCredentialsException();

        userLoginRequest.setPassword(AES.encrypt(userLoginRequest.getPassword()));
        var userDetails = retrieveUserDetailsByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());

        var user = this.userRepository.findUserEntityByUserDetailsEntity(userDetails);
        var userAuthorization = user.getUserAuthorizationEntity();
        this.updateAccessToken(userAuthorization);

        var loginUserResponse = this.responseFactory.createLoginUserResponse(userAuthorization.getToken(), userDetails.getEmail(), userAuthorization.getRoles(), user.getId());

        log.info("Successfully logged user - " + loginUserResponse);
        return loginUserResponse;
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
    public void logoutUser(Long userId) throws UserNotFoundException {
        log.info("Started logout user with id - " + userId);

        var user = this.findUserByUserId(userId);

        var userAuthorization = user.getUserAuthorizationEntity();
        this.resetUserAccessToken(userAuthorization);

        log.info("Successfully logged out user with id - " + userId);
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
    public void authorizeUser(Long userId) throws UnauthorizedUserException {
        var request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        var requesterId = this.getUserIdFromRequestOrThrowUnauthorizedException(request);
        log.info("Started user authorization with provided id - " + userId);

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user) || !userId.equals(requesterId))
            throw new UnauthorizedUserException();

        log.info("Successfully authorized user with provided id - " + userId);
    }

    @Override
    public void authorizeAdmin() throws UnauthorizedUserException {
        var request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        var requesterId = this.getUserIdFromRequestOrThrowUnauthorizedException(request);
        log.info("Started admin authorization with provided id - " + requesterId);

        var user = this.userRepository.findUserEntityById(requesterId);
        if (user.getUserAuthorizationEntity().getRoles()
                .stream()
                .noneMatch(r -> r.getName().equals("ROLE_ADMIN")))
            throw new UnauthorizedUserException();

        log.info("Successfully authorized admin with provided id - " + requesterId);
    }

    private Long getUserIdFromRequestOrThrowUnauthorizedException(HttpServletRequest request) throws UnauthorizedUserException {
        try{
            return Long.parseLong(request.getHeader("X-USER-ID"));
        }catch(NumberFormatException e){
            throw new UnauthorizedUserException();
        }
    }
}
