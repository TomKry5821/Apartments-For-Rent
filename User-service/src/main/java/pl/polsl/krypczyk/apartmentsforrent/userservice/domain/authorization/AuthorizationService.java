package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

public interface AuthorizationService {

    CreateUserResponse registerNewUser(CreateUserRequest createUserRequest) throws BadCredentialsException, UserAlreadyExistsException;
    LoginUserResponse loginUser(UserLoginRequest userLoginRequest) throws BadCredentialsException, InactiveAccountException, UserNotFoundException;
    void logoutUser(Long userId) throws InactiveAccountException, UserNotFoundException;
    void authorizeUser(Long userId, Long requesterId) throws UnauthorizedUserException;
    void authorizeAdmin(Long requesterId) throws UnauthorizedUserException;
}
