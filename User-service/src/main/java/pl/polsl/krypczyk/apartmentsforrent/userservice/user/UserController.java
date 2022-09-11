package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.ChangeUserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

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
    public UserDetailsDTO getUserDetails(@PathVariable("userId") @NotNull Long userId,
                                         @RequestHeader("Authorization") UUID accessToken) {
        this.authorizationService.authorizeUser(userId, accessToken);
        return this.userService.getUserDetails(userId);
    }

    @PutMapping("users/{userId}/details")
    public ChangeUserDetailsDTO changeUserDetails(@RequestBody @Valid ChangeUserDetailsDTO changeUserDetailsDTO,
                                                  @PathVariable("userId") @NotNull Long userId,
                                                  @RequestHeader("Authorization") UUID accessToken) {
        this.authorizationService.authorizeUser(userId, accessToken);
        return this.userService.changeUserDetails(changeUserDetailsDTO, userId);
    }

    @PostMapping("users/{userId}/inactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivateAccount(@PathVariable("userId") @NotNull Long userId,
                                  @RequestHeader("Authorization") UUID accessToken) {
        this.authorizationService.authorizeUser(userId, accessToken);
        this.userService.inactivateAccount(userId);
    }
}

