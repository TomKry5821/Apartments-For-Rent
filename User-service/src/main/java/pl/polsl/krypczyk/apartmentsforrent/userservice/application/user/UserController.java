package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("user/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @PostMapping("/public/users")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse registerNewUser(@Valid @RequestBody CreateUserRequest createUserRequest) throws UserAlreadyExistsException {
        return this.userService.createUser(createUserRequest);
    }

    @GetMapping("users/{userId}/details")
    public GetUserDetailsResponse getUserDetails(@PathVariable @NotNull @Min(value = 1) Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeUser(userId);
        return this.userService.getUserDetails(userId);
    }

    @PutMapping("users/{userId}/details")
    public ChangeUserDetailsResponse changeUserDetails(@RequestBody @Valid ChangeUserDetailsRequest changeUserDetailsRequest,
                                                       @PathVariable @NotNull @Min(value = 1) Long userId) throws UnauthorizedUserException, UserNotFoundException, InvalidUserDetailsException, UserAlreadyExistsException {
        this.authorizationService.authorizeUser(userId);
        return this.userService.changeUserDetails(changeUserDetailsRequest, userId);
    }

    @PostMapping("users/{userId}/inactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivateAccount(@PathVariable @NotNull @Min(value = 1) Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeUser(userId);
        this.userService.inactivateAccount(userId);
    }
}

