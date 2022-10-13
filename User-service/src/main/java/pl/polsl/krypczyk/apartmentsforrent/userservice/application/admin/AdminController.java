package pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.UnauthorizedUserException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("user/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthorizationService authorizationService;

    @GetMapping("/users")
    public Collection<UserDTO> getAllUsers() throws UnauthorizedUserException {
        this.authorizationService.authorizeAdmin();
        return this.adminService.getAllUsers();
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteUser(@PathVariable("userId") @NotNull Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeAdmin();
        this.adminService.deleteUser(userId);
    }

    @PutMapping("users/{userId}/details")
    public ChangeUserDetailsResponse changeUserDetails(@RequestBody @Valid ChangeUserDetailsRequest changeUserDetailsRequest,
                                                       @PathVariable("userId") @NotNull Long userId) throws UnauthorizedUserException, UserNotFoundException, InvalidUserDetailsException {
        this.authorizationService.authorizeAdmin();
        return this.adminService.changeUserDetails(changeUserDetailsRequest, userId);
    }

    @PostMapping("users/{userId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateAccount(@PathVariable("userId") @NotNull Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeAdmin();
        this.adminService.activateAccount(userId);
    }
}
