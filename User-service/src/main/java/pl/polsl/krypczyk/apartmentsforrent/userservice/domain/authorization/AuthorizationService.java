package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;

import java.util.UUID;

public interface AuthorizationService {

    CreateUserResponse registerNewUser(CreateUserRequest createUserRequest);

    LoginUserResponse loginUser(UserLoginRequest userLoginRequest);

    void logoutUser(Long userId);

    void authorizeAdmin(UUID accessToken);

    void deleteDbContent();

    void authorizeUser(Long userId, UUID accessToken);
}
