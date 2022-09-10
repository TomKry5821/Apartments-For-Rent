package pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "Invalid credentials")
public class UserLoginRequestDTO {

    @Email(message = "Invalid e-mail")
    private final String email;

    @NotEmpty(message = "Invalid password")
    private final String password;
}
