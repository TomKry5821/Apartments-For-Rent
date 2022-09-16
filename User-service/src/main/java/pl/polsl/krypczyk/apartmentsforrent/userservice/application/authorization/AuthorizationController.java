package pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.annotation.uuid;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse registerNewUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return this.authorizationService.registerNewUser(createUserRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginUserResponse loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return this.authorizationService.loginUser(userLoginRequest);
    }

    @PostMapping("{userId}/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutUser(@PathVariable("userId") Long userId,
                           @RequestHeader("requester-user-id") @NotNull Long requesterId) {
        this.authorizationService.authorizeUser(userId, requesterId);
        this.authorizationService.logoutUser(userId);
    }

}
