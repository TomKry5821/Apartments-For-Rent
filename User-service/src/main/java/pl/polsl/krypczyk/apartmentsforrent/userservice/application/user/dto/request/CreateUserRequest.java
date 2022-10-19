package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NotNull(message = "Invalid user details")
public class CreateUserRequest {

    @NotEmpty(message = "Invalid name")
    private String name;

    @NotEmpty(message = "Invalid surname")
    private String surname;

    @Email(message = "Invalid e-mail")
    private String email;

    @NotNull(message = "Invalid is active status")
    private Boolean isActive;

    @NotEmpty(message = "Invalid password")
    private String password;
}
