package pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.response.GetAllUsersResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.annotation.uuid;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("user/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthorizationService authorizationService;

    @GetMapping("/users")
    public GetAllUsersResponse getAllUsers(@RequestHeader("Authorization") @uuid UUID accessToken) {
        this.authorizationService.authorizeAdmin(accessToken);
        return this.adminService.getAllUsers();
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteUser(@PathVariable("userId") @NotNull Long userId,
                           @RequestHeader("Authorization") @uuid UUID accessToken) {
        this.authorizationService.authorizeAdmin(accessToken);
        this.adminService.deleteUser(userId);
    }
}
