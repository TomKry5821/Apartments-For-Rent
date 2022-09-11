package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.ChangeUserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("user/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("users/{userId}/details")
    public UserDetailsDTO getUserDetails(@PathVariable("userId") @NotNull Long userId){
        return this.userService.getUserDetails(userId);
    }

    @PutMapping("users/details")
    public ChangeUserDetailsDTO changeUserDetails(@RequestBody ChangeUserDetailsDTO changeUserDetailsDTO){
        return this.userService.changeUserDetails(changeUserDetailsDTO);
    }
}

