package pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull
@Builder
public class ChangeUserDetailsDTO {

    @NotNull(message = "User id cannot be null")
    private Long userId;

    private String name;

    private String surname;

    private String email;

    private Boolean isActive;

    private String password;

}
