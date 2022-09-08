package pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDTO {

    private String name;

    private String surname;

    private String email;

    private Boolean isActive;

    private String password;
}
