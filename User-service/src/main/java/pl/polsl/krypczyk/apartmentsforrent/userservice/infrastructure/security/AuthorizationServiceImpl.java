package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final ResponseFactory responseFactory;

    @Override
    public LoginUserResponse loginUser(UserLoginRequest userLoginRequest) throws UserNotFoundException {
        log.info("Started logging user with details -" + userLoginRequest);

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
        var request = getRequest();
        var requesterId = this.getUserIdFromRequestOrThrowUnauthorizedException(request);
        var roles = this.getUserRolesFromRequest(request);

        log.info("Started user authorization with provided id - " + userId);

        if (!userId.equals(requesterId) || !roles.contains(ROLE_USER))
            throw new UnauthorizedUserException();

        log.info("Successfully authorized user with provided id - " + userId);
    }

    @Override
    public void authorizeAdmin() throws UnauthorizedUserException {
        var request = getRequest();
        var roles = this.getUserRolesFromRequest(request);

        log.info("Started admin authorization");

        if (!roles.contains(ROLE_ADMIN))
            throw new UnauthorizedUserException();

        log.info("Successfully authorized admin");
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    private Long getUserIdFromRequestOrThrowUnauthorizedException(HttpServletRequest request) throws UnauthorizedUserException {
        try {
            return Long.parseLong(request.getHeader("X-USER-ID"));
        } catch (NumberFormatException e) {
            throw new UnauthorizedUserException();
        }
    }

    private Collection<String> getUserRolesFromRequest(HttpServletRequest request) {

        var userRoles = request.getHeader("X-USER-ROLES")
                .replace("[", "")
                .replace("]", "")
                .split(", ");

        return List.of(userRoles);
    }
}
