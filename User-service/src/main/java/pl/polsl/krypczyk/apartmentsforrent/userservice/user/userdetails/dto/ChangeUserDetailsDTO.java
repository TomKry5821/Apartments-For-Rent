package pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull
@Builder
public class ChangeUserDetailsDTO {

    private String name;

    private String surname;

    private String email;

    private Boolean isActive;

    private String password;

}
