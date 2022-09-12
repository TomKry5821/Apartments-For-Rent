package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.annotation.uuid;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("user/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteUser(@PathVariable("userId") @NotNull Long userId,
                           @RequestHeader("Authorization") @uuid UUID accessToken) {
        this.authorizationService.authorizeAdmin(accessToken);
        this.userService.deleteUser(userId);
    }
}
