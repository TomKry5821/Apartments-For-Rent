package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.annotation.uuid;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("user/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @GetMapping("users/{userId}/details")
    public GetUserDetailsResponse getUserDetails(@PathVariable("userId") @NotNull Long userId,
                                                 @RequestHeader("requester-user-id") @NotNull Long requesterId) throws UnauthorizedUserException, UserNotFoundException, InactiveAccountException {
        this.authorizationService.authorizeUser(userId, requesterId);
        return this.userService.getUserDetails(userId);
    }

    @PutMapping("users/{userId}/details")
    public ChangeUserDetailsResponse changeUserDetails(@RequestBody @Valid ChangeUserDetailsRequest changeUserDetailsRequest,
                                                       @PathVariable("userId") @NotNull Long userId,
                                                       @RequestHeader("requester-user-id") @NotNull Long requesterId) throws UnauthorizedUserException, UserNotFoundException, InactiveAccountException, InvalidUserDetailsException, UserAlreadyExistsException {
        this.authorizationService.authorizeUser(userId, requesterId);
        return this.userService.changeUserDetails(changeUserDetailsRequest, userId);
    }

    @PostMapping("users/{userId}/inactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivateAccount(@PathVariable("userId") @NotNull Long userId,
                                  @RequestHeader("requester-user-id") @NotNull Long requesterId) throws UnauthorizedUserException, UserNotFoundException, InactiveAccountException {
        this.authorizationService.authorizeUser(userId, requesterId);
        this.userService.inactivateAccount(userId);
    }
}

