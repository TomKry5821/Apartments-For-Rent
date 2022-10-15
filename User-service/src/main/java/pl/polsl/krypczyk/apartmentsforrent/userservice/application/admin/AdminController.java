package pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.security.exception.UnauthorizedUserException;
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
    public void DeleteUser(@PathVariable @NotNull Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeAdmin();
        this.adminService.deleteUser(userId);
    }

    @PutMapping("users/{userId}/details")
    public ChangeUserDetailsResponse changeUserDetails(@RequestBody @Valid ChangeUserDetailsRequest changeUserDetailsRequest,
                                                       @PathVariable @NotNull Long userId) throws UnauthorizedUserException, UserNotFoundException, InvalidUserDetailsException {
        this.authorizationService.authorizeAdmin();
        return this.adminService.changeUserDetails(changeUserDetailsRequest, userId);
    }

    @PostMapping("users/{userId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateAccount(@PathVariable @NotNull Long userId) throws UnauthorizedUserException, UserNotFoundException {
        this.authorizationService.authorizeAdmin();
        this.adminService.activateAccount(userId);
    }
}
