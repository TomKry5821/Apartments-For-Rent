package pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UserDetailsDTO {

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

    @NotNull(message = "Invalid creation date")
    private LocalDateTime creationDate;
}
