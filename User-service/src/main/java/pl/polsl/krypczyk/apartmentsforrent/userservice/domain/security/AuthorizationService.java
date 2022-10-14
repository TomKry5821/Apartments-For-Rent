package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

public interface AuthorizationService {

    LoginUserResponse loginUser(UserLoginRequest userLoginRequest) throws UserNotFoundException;

    void logoutUser(Long userId) throws UserNotFoundException;

    void authorizeUser(Long userId) throws UnauthorizedUserException;

    void authorizeAdmin() throws UnauthorizedUserException;
}
