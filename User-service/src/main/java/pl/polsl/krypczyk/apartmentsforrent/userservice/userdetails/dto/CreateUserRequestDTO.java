package pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NotNull(message = "Invalid user details")
public class CreateUserRequestDTO {

    @NotEmpty(message = "Invalid name")
    private String name;

    @NotEmpty(message = "Invalid surname")
    private String surname;

    @Email(message = "Invalid e-mail")
    private String email;

    @NotEmpty(message = "Invalid is active account")
    private Boolean isActive;

    @NotEmpty(message = "Invalid password")
    private String password;
}
