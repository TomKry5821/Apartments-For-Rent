package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "Invalid credentials")
public class UserLoginRequest {

    @Email(message = "Invalid e-mail")
    private String email;

    @NotEmpty(message = "Invalid password")
    private String password;
}
