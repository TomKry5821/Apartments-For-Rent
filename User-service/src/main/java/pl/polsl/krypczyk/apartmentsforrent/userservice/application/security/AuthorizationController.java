package pl.polsl.krypczyk.apartmentsforrent.userservice.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.UserLoginRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response.LoginUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginUserResponse loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) throws UserNotFoundException {
        return this.authorizationService.loginUser(userLoginRequest);
    }

    @PostMapping("{userId}/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutUser(@PathVariable @NotNull @Min(value = 1) Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeUser(userId);
        this.authorizationService.logoutUser(userId);
    }

}
